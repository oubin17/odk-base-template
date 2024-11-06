package com.odk.basemanager.deal.user;

import com.alibaba.fastjson.JSONObject;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.UserAccessTokenDO;
import com.odk.basedomain.domain.UserBaseDO;
import com.odk.basedomain.domain.UserIdentificationDO;
import com.odk.basedomain.repository.UserAccessTokenRepository;
import com.odk.basedomain.repository.UserBaseRepository;
import com.odk.basedomain.repository.UserIdentificationRepository;
import com.odk.basemanager.dto.UserRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.UUID;

/**
 * UserRegisterManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserRegisterManager {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisterManager.class);

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserIdentificationRepository identificationRepository;

    private TransactionTemplate transactionTemplate;

    public String registerUser(UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValue(userRegisterDTO.getLoginType(), userRegisterDTO.getLoginId());
        AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED);
        String userId = UUID.randomUUID().toString();

        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    addUserBase(userId, userRegisterDTO);
                    addAccessToken(userId, userRegisterDTO);
                    addIdentification(userId, userRegisterDTO);
                }
            });
        }catch (DataIntegrityViolationException exception) {
            logger.error("注册发生唯一键冲突：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), exception);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (DuplicateKeyException duplicateKeyException) {
            logger.error("注册发生主键冲突：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), duplicateKeyException);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (Exception e) {
            logger.error("注册发生未知异常，注册信息：{}, 异常原因：", JSONObject.toJSONString(userRegisterDTO), e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR);
        }

        return userId;
    }

    /**
     * 添加基础信息
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addUserBase(String userId, UserRegisterDTO userRegisterDTO) {
        UserBaseDO userBase = new UserBaseDO();
        userBase.setUserId(userId);
        userBase.setUserType(UserTypeEnum.INDIVIDUAL.getCode());
        userBase.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userBase.setUserName(userRegisterDTO.getUserName());
        userBaseRepository.save(userBase);
    }

    /**
     * 添加登录手机号
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addAccessToken(String userId, UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO accessToken = new UserAccessTokenDO();
        accessToken.setTokenId(UUID.randomUUID().toString());
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
    private void addIdentification(String userId, UserRegisterDTO userRegisterDTO) {
        UserIdentificationDO identification = new UserIdentificationDO();
        identification.setIdentifyId(UUID.randomUUID().toString());
        identification.setUserId(userId);
        identification.setIdentifyType(IdentificationTypeEnum.PASSWORD.getCode());
        identification.setIdentifyValue(userRegisterDTO.getPassword());
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
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}
