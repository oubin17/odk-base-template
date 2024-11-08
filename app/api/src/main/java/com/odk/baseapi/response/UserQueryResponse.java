package com.odk.baseapi.response;

import lombok.Data;

/**
 * UserQueryResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Data
public class UserQueryResponse {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

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

}
