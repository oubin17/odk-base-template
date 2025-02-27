package com.odk.baseutil.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * UserEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@Data
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3674637208987065622L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户类型
     * {@link com.odk.base.enums.user.UserTypeEnum}
     */
    private String userType;

    /**
     * 用户状态
     * {@link com.odk.base.enums.user.UserStatusEnum}
     */
    private String userStatus;

    /**
     * 登录账号
     */
    private AccessTokenEntity accessToken;

    /**
     * 用户画像
     */
    private UserProfileEntity userProfile;

}
