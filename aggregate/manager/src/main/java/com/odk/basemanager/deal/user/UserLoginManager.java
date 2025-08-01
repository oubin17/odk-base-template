package com.odk.basemanager.deal.user;

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
import com.odk.baseinfra.security.IDecrypt;
import com.odk.baseinfra.security.IEncrypt;
import com.odk.basemanager.api.user.IUserLoginManager;
import com.odk.basemanager.deal.verificationcode.VerificationCodeManager;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.UserQueryTypeEnum;
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

    private IDecrypt iDecrypt;

    private IEncrypt iEncrypt;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserEntity userLogin(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = null;
        String identifyType = userLoginDTO.getIdentifyType();
        if (IdentificationTypeEnum.PASSWORD.getCode().equals(identifyType)) {
            UserQueryCriteria build = UserQueryCriteria.builder()
                    .queryType(UserQueryTypeEnum.LOGIN_ID)
                    .loginId(userLoginDTO.getLoginId())
                    .loginType(userLoginDTO.getLoginType())
                    .build();
            userEntity = userQueryDomain.queryUser(build);
            UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(userEntity.getUserId(), userLoginDTO.getIdentifyType(), TenantIdContext.getTenantId());

            String decrypt = iDecrypt.decrypt(userLoginDTO.getIdentifyValue());
            AssertUtil.isTrue(iEncrypt.matches(decrypt, userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        } else if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(identifyType)) {
            verificationCodeManager.compareAndIncr(userLoginDTO.getVerificationCode());
            UserQueryCriteria build = UserQueryCriteria.builder()
                    .queryType(UserQueryTypeEnum.LOGIN_ID)
                    .loginId(userLoginDTO.getLoginId())
                    .loginType(userLoginDTO.getLoginType())
                    .build();
            userEntity = userQueryDomain.queryUser(build);
        }
        if (userEntity != null) {
            //设置登录session
            SessionContext.createLoginSession(userEntity.getUserId());
            //缓存当前用户信息
            SessionContext.setSessionValue(UserInfoConstants.ACCOUNT_SESSION_USER, userEntity);
        }
        return userEntity;
    }

    @Override
    public Boolean userLogout() {
        Optional<UserBaseDO> byUserId = baseRepository.findByIdAndTenantId(SessionContext.getLoginIdWithCheck(), TenantIdContext.getTenantId());
        AssertUtil.isTrue(byUserId.isPresent(), BizErrorCode.USER_NOT_EXIST, "用户ID不存在");
        SessionContext.logOut();
        return true;
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
    public void setiDecrypt(IDecrypt iDecrypt) {
        this.iDecrypt = iDecrypt;
    }

    @Autowired
    public void setiEncrypt(IEncrypt iEncrypt) {
        this.iEncrypt = iEncrypt;
    }
}
