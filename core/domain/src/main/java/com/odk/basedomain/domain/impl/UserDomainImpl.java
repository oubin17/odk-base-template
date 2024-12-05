package com.odk.basedomain.domain.impl;

import com.alibaba.fastjson.JSONObject;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.PasswordDomain;
import com.odk.basedomain.domain.UserDomain;
import com.odk.basedomain.domodel.user.UserAccessTokenDO;
import com.odk.basedomain.domodel.user.UserBaseDO;
import com.odk.basedomain.domodel.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.baseutil.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

/**
 * UserDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Slf4j
@Service
public class UserDomainImpl implements UserDomain {

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserIdentificationRepository identificationRepository;

    private PasswordDomain passwordDomain;

    private TransactionTemplate transactionTemplate;

    @Override
    public Long registerUser(UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValue(userRegisterDTO.getLoginType(), userRegisterDTO.getLoginId());
        AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, "用户已经存在，类型：" + userRegisterDTO.getLoginType() + "，登录ID：" + userRegisterDTO.getLoginId());
        Long userId;
        try {
            userId = transactionTemplate.execute(status -> {
                Long userId1 = addUserBase(userRegisterDTO);
                addAccessToken(userId1, userRegisterDTO);
                //密码加密
                String password = userRegisterDTO.getIdentifyValue();
                userRegisterDTO.setIdentifyValue(passwordDomain.encode(password));
                addIdentification(userId1, userRegisterDTO);
                return userId1;
            });
        } catch (DataIntegrityViolationException exception) {
            log.error("注册发生唯一键冲突：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), exception);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (DuplicateKeyException duplicateKeyException) {
            log.error("注册发生主键冲突：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), duplicateKeyException);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (Exception e) {
            log.error("注册发生未知异常，注册信息：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR);
        }

        return userId;
    }

    /**
     * 添加基础信息
     *
     * @param userRegisterDTO
     */
    private Long addUserBase(UserRegisterDTO userRegisterDTO) {
        UserBaseDO userBase = new UserBaseDO();
        userBase.setUserType(UserTypeEnum.INDIVIDUAL.getCode());
        userBase.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userBase.setUserName(userRegisterDTO.getUserName());
        UserBaseDO save = userBaseRepository.save(userBase);
        return save.getId();
    }

    /**
     * 添加登录手机号
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addAccessToken(Long userId, UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO accessToken = new UserAccessTokenDO();
        accessToken.setUserId(userId);
        accessToken.setTokenType(userRegisterDTO.getLoginType());
        accessToken.setTokenValue(userRegisterDTO.getLoginId());
        accessTokenRepository.save(accessToken);
    }

    /**
     * 添加密码
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addIdentification(Long userId, UserRegisterDTO userRegisterDTO) {
        UserIdentificationDO identification = new UserIdentificationDO();
        identification.setUserId(userId);
        identification.setIdentifyType(userRegisterDTO.getIdentifyType());
        identification.setIdentifyValue(userRegisterDTO.getIdentifyValue());
        identificationRepository.save(identification);
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setIdentificationRepository(UserIdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    @Autowired
    public void setPasswordDomain(PasswordDomain passwordDomain) {
        this.passwordDomain = passwordDomain;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}
