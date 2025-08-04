package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.UserProfileRequest;

/**
 * UserProfileApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
public interface UserProfileApi {

    /**
     * 更新用户信息
     *
     * @return
     */
    ServiceResponse<Boolean> updateUserProfile(UserProfileRequest userProfileRequest);
}
