package com.odk.baseservice.impl.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserProfileApi;
import com.odk.basemanager.api.user.IUserProfileManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.convert.UserProfileRequestConvert;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.UserProfileRequest;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserProfileService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserProfileService implements UserProfileApi {

    private UserProfileRequestConvert userProfileRequestConvert;

    private IUserProfileManager userProfileManager;

    @Override
    @BizProcess(bizScene = BizScene.USER_PROFILE_UPDATE)
    public ServiceResponse<Boolean> updateUserProfile(UserProfileRequest userProfileRequest) {
        return ServiceResponse.valueOfSuccess(userProfileManager.updateUserProfile(SessionContext.getLoginIdWithCheck(), userProfileRequestConvert.toDTO(userProfileRequest)));
    }

}
