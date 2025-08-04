package com.odk.basemanager.api.user;

import com.odk.baseutil.dto.user.UserProfileDTO;

/**
 * IUserProfileManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
public interface IUserProfileManager {

    /**
     * 更新用户信息
     *
     * @param userProfileDTO
     */
    boolean updateUserProfile(UserProfileDTO userProfileDTO);
}
