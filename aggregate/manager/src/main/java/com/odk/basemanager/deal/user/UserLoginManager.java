package com.odk.basemanager.deal.user;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.PasswordDomain;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domodel.user.UserBaseDO;
import com.odk.basedomain.domodel.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
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

    private UserIdentificationRepository identificationRepository;

    private PasswordDomain passwordDomain;

    private UserQueryDomain userQueryDomain;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
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

    public Boolean userLogout(String userId) {
        Optional<UserBaseDO> byUserId = baseRepository.findById(userId);
        AssertUtil.isTrue(byUserId.isPresent(), BizErrorCode.USER_NOT_EXIST, "用户ID不存在");
        AssertUtil.isTrue(byUserId.get().getId().equals(SessionContext.getLoginIdAsString()), BizErrorCode.TOKEN_UNMATCHED);
        SessionContext.logOut();
        return true;
    }

    @Autowired
    public void setIdentificationRepository(UserIdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setPasswordDomain(PasswordDomain passwordDomain) {
        this.passwordDomain = passwordDomain;
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }
}
