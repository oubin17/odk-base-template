package com.odk.basedomain.domain;

import com.odk.baseutil.entity.UserProfileEntity;

/**
 * UserProfileDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public interface UserProfileDomain {

    /**
     * 获取用户画像
     *
     * @param userId
     * @return
     */
    UserProfileEntity getUserProfileByUserId(String userId);
}
