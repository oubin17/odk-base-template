package com.odk.basemanager.deal.user;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.dataobject.user.UserBaseDO;
import com.odk.basedomain.domain.UserDomain;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.repository.user.UserBaseRepository;
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
public class UserLoginManager {

    private UserBaseRepository baseRepository;

    private UserDomain userDomain;

    private VerificationCodeManager verificationCodeManager;

    private UserQueryDomain userQueryDomain;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    public UserEntity userLogin(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = null;
        String identifyType = userLoginDTO.getIdentifyType();
        if (IdentificationTypeEnum.PASSWORD.getCode().equals(identifyType)) {
            userEntity = this.userDomain.userLogin(userLoginDTO);
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

    public Boolean userLogout() {
        Optional<UserBaseDO> byUserId = baseRepository.findById(SessionContext.getLoginIdWithCheck());
        AssertUtil.isTrue(byUserId.isPresent(), BizErrorCode.USER_NOT_EXIST, "用户ID不存在");
        SessionContext.logOut();
        return true;
    }


    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }

    @Autowired
    public void setVerificationCodeManager(VerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }
}
