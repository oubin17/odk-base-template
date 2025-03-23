package com.odk.basedomain.domain.impl;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.JacksonUtil;
import com.odk.basedomain.domain.PasswordDomain;
import com.odk.basedomain.domain.UserDomain;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.dataobject.user.UserAccessTokenDO;
import com.odk.basedomain.dataobject.user.UserBaseDO;
import com.odk.basedomain.dataobject.user.UserIdentificationDO;
import com.odk.basedomain.dataobject.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.dto.user.PasswordResetDTO;
import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.time.LocalDateTime;

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

    private UserQueryDomain userQueryDomain;

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserIdentificationRepository identificationRepository;

    private UserProfileRepository userProfileRepository;

    private PasswordDomain passwordDomain;

    private TransactionTemplate transactionTemplate;

    @Override
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValue(userRegisterDTO.getLoginType(), userRegisterDTO.getLoginId());
        AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, "用户已经存在，类型：" + userRegisterDTO.getLoginType() + "，登录ID：" + userRegisterDTO.getLoginId());
        String userId;
        try {
            userId = transactionTemplate.execute(status -> {
                //1.生成用户id
                String userId1 = addUserBase();
                //2.添加登录信息
                addAccessToken(userId1, userRegisterDTO);
                //3.添加密码信息 密码加密
                String password = userRegisterDTO.getIdentifyValue();
                userRegisterDTO.setIdentifyValue(passwordDomain.encode(password));
                addIdentification(userId1, userRegisterDTO);
                //4.添加用户画像
                addUserProfile(userId1, userRegisterDTO.getUserName());
                return userId1;
            });
        } catch (DataIntegrityViolationException exception) {
            log.error("注册发生唯一键冲突：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), exception);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (DuplicateKeyException duplicateKeyException) {
            log.error("注册发生主键冲突：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), duplicateKeyException);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (Exception e) {
            log.error("注册发生未知异常，注册信息：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR);
        }

        return userId;
    }

    @Override
    public UserEntity userLogin(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userQueryDomain.queryByLoginTypeAndLoginId(userLoginDTO.getLoginType(), userLoginDTO.getLoginId());
        AssertUtil.notNull(userEntity, BizErrorCode.USER_NOT_EXIST);
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyType(userEntity.getUserId(), userLoginDTO.getIdentifyType());
        AssertUtil.isTrue(passwordDomain.matches(userLoginDTO.getIdentifyValue(), userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        //设置登录session
        SessionContext.createLoginSession(userEntity.getUserId());
        //缓存当前用户信息
        SessionContext.setSessionValue(UserInfoConstants.ACCOUNT_SESSION_USER, userEntity);
        return userEntity;
    }

    @Override
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        //1.检查登录态
        SessionContext.checkLogin();

        //2.比对旧密码
        UserEntity userEntity = userQueryDomain.queryByUserIdAndCheck(SessionContext.getLoginIdWithCheck());
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyType(userEntity.getUserId(), passwordUpdateDTO.getIdentifyType());
        AssertUtil.isTrue(passwordDomain.matches(passwordUpdateDTO.getOldIdentifyValue(), userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);

        //4.对比新密码
        String encode = passwordDomain.encode(passwordUpdateDTO.getNewIdentifyValue());
        AssertUtil.isFalse(passwordDomain.matches(passwordUpdateDTO.getOldIdentifyValue(), encode), BizErrorCode.IDENTIFICATION_SAME);

        //5.设置新密码
        int count = this.identificationRepository.updatePassword(userIdentificationDO.getId(), passwordUpdateDTO.getIdentifyType(), encode, userEntity.getUserId(), LocalDateTime.now());
        return count > 0;
    }

    @Override
    public boolean resetPassword(PasswordResetDTO resetDTO) {
        //这里需要有些安全校验

        return false;
    }

    /**
     * 添加基础信息
     *
     */
    private String addUserBase() {
        UserBaseDO userBase = new UserBaseDO();
        userBase.setUserType(UserTypeEnum.INDIVIDUAL.getCode());
        userBase.setUserStatus(UserStatusEnum.NORMAL.getCode());
        UserBaseDO save = userBaseRepository.save(userBase);
        return save.getId();
    }

    /**
     * 添加登录手机号
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addAccessToken(String userId, UserRegisterDTO userRegisterDTO) {
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
    private void addIdentification(String userId, UserRegisterDTO userRegisterDTO) {
        UserIdentificationDO identification = new UserIdentificationDO();
        identification.setUserId(userId);
        identification.setIdentifyType(userRegisterDTO.getIdentifyType());
        identification.setIdentifyValue(userRegisterDTO.getIdentifyValue());
        identificationRepository.save(identification);
    }

    /**
     * 添加用户画像
     *
     * @param userId
     * @param userName
     */
    private void addUserProfile(String userId, String userName) {
        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setUserId(userId);
        userProfileDO.setUserName(userName);
        userProfileRepository.save(userProfileDO);
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

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
}
