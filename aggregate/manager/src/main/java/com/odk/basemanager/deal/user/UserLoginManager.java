package com.odk.basemanager.deal.user;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.user.UserAccessTokenDO;
import com.odk.basedomain.domain.user.UserBaseDO;
import com.odk.basedomain.domain.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.basemanager.deal.password.PasswordManager;
import com.odk.basemanager.dto.UserLoginDTO;
import com.odk.basemanager.entity.UserLoginEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private UserAccessTokenRepository accessTokenRepository;

    private UserIdentificationRepository identificationRepository;

    private PasswordManager passwordManager;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    public UserLoginEntity userLogin(UserLoginDTO userLoginDTO) {
        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValue(userLoginDTO.getLoginType(), userLoginDTO.getLoginId());
        AssertUtil.notNull(userAccessTokenDO, BizErrorCode.USER_NOT_EXIST);
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyType(userAccessTokenDO.getUserId(), userLoginDTO.getIdentifyType());
        AssertUtil.isTrue(passwordManager.matches(userLoginDTO.getIdentifyValue(), userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        UserLoginEntity userLoginVO = new UserLoginEntity();
        userLoginVO.setUserId(userAccessTokenDO.getUserId());
        return userLoginVO;
    }

    public Boolean userLogout(String userId) {
        UserBaseDO byUserId = baseRepository.findByUserId(userId);
        AssertUtil.notNull(byUserId, BizErrorCode.USER_NOT_EXIST, "用户ID不存在");
        AssertUtil.isTrue(StringUtils.equals(byUserId.getUserId(), StpUtil.getLoginIdAsString()), BizErrorCode.TOKEN_UNMATCHED);
        return true;
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
    public void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }
}
