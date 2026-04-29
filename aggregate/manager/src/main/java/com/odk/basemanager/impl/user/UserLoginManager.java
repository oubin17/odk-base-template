package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.baseinfra.security.IEncryption;
import com.odk.basemanager.api.user.IUserLoginManager;
import com.odk.basemanager.impl.verificationcode.VerificationCodeManager;
import com.odk.baseutil.context.DeviceInfoContext;
import com.odk.baseutil.convert.UserLoginConvert;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserLoginManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserLoginManager implements IUserLoginManager {

    private UserBaseRepository baseRepository;

    private VerificationCodeManager verificationCodeManager;

    private UserQueryDomain userQueryDomain;

    private UserIdentificationRepository identificationRepository;

    private IEncryption encryption;

//    private IEventPublish eventPublish;

    private UserLoginConvert userLoginConvert;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserLoginResponse userLogin(UserLoginDTO userLoginDTO) {
        UserQueryCriteria build = UserQueryCriteria.builder()
                .queryType(UserQueryTypeEnum.LOGIN_ID)
                .loginId(userLoginDTO.getLoginId())
                .loginType(userLoginDTO.getLoginType())
                .statusCheck(true)
                .build();
        UserEntity userEntity = userQueryDomain.queryUser(build);
        if (IdentificationTypeEnum.PASSWORD.getCode().equals(userLoginDTO.getIdentifyType())) {
            UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(userEntity.getUserId(), userLoginDTO.getIdentifyType(), TenantIdContext.getTenantId());
            String decrypt = encryption.rsaDecode(userLoginDTO.getIdentifyValue());
            AssertUtil.isTrue(encryption.bcryptMatches(decrypt, userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        } else if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(userLoginDTO.getIdentifyType())) {
            verificationCodeManager.compareAndIncr(userLoginDTO.getVerificationCode());
        }

        return setLoginSession(userEntity);
    }

    @Override
    public UserLoginResponse userLoginAfterRegister(String userId) {
        UserQueryCriteria build = UserQueryCriteria.builder()
                .queryType(UserQueryTypeEnum.USER_ID)
                .userId(userId)
                .statusCheck(true)
                .build();
        UserEntity userEntity = userQueryDomain.queryUser(build);
        return setLoginSession(userEntity);
    }


    @Override
    public Boolean userLogout() {
        Optional<UserBaseDO> byUserId = baseRepository.findByIdAndTenantId(SessionContext.getLoginIdWithCheck(), TenantIdContext.getTenantId());
        AssertUtil.isTrue(byUserId.isPresent(), BizErrorCode.USER_NOT_EXIST);
//        eventPublish.publish(new UserCacheCleanEvent(byUserId.get().getId(), UserCacheSceneEnum.USER_BASIC, CacheActionEnum.DELETE));
        SessionContext.logOut(SessionContext.getLoginIdWithCheck(), DeviceInfoContext.get().getDeviceType());
        return true;
    }

    /**
     * 登录成功后，设置session
     *
     * @param userEntity
     * @return
     */
    private UserLoginResponse setLoginSession(UserEntity userEntity) {
        //这里说明登录校验通过
        UserLoginResponse response = userLoginConvert.toResponse(userEntity);
        //设置登录session
        SessionContext.createLoginSession(userEntity.getUserId(), DeviceInfoContext.get().getDeviceType());
//        SessionContext.setSessionValue("deviceInfo", DeviceInfoContext.get());
        response.setToken(SessionContext.getToken());
        return response;
    }

    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setVerificationCodeManager(VerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }

    @Autowired
    public void setIdentificationRepository(UserIdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    @Autowired
    public void setEncryption(IEncryption encryption) {
        this.encryption = encryption;
    }

    @Autowired
    public void setUserLoginConvert(UserLoginConvert userLoginConvert) {
        this.userLoginConvert = userLoginConvert;
    }


    //
//    @Autowired
//    public void setEventPublish(IEventPublish eventPublish) {
//        this.eventPublish = eventPublish;
//    }
}
