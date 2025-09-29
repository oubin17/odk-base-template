package com.odk.baseservice.impl.user;

import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserProfileApi;
import com.odk.basemanager.api.user.IUserProfileManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.UserProfileDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.mapper.UserProfileRequestMapper;
import com.odk.baseutil.request.UserProfileRequest;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserProfileService extends AbstractApiImpl implements UserProfileApi {

    private UserProfileRequestMapper userProfileRequestMapper;

    private IUserProfileManager userProfileManager;

    @Override
    public ServiceResponse<Boolean> updateUserProfile(UserProfileRequest userProfileRequest) {
        return super.strictBizProcess(BizScene.USER_PROFILE_UPDATE, userProfileRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected Object convert(BaseRequest request) {
                UserProfileRequest profileRequest = (UserProfileRequest) request;
                return userProfileRequestMapper.toDTO(profileRequest);
            }

            @Override
            protected Boolean doProcess(Object args) {
                UserProfileDTO profileRequest = (UserProfileDTO) args;
                return userProfileManager.updateUserProfile(SessionContext.getLoginIdWithCheck(), profileRequest);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }
        });
    }


    @Autowired
    public void setUserProfileRequestMapper(UserProfileRequestMapper userProfileRequestMapper) {
        this.userProfileRequestMapper = userProfileRequestMapper;
    }

    @Autowired
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }
}
