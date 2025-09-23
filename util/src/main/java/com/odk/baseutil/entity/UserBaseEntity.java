package com.odk.baseutil.entity;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * UserBaseEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Data
public class UserBaseEntity implements Serializable {

    private String id;

    @Serial
    private static final long serialVersionUID = -72193556701631772L;
    /**
     * 用户类型
     * {@link UserTypeEnum}
     */
    private String userType;

    /**
     * 用户状态
     * {@link UserStatusEnum}
     */
    private String userStatus;

}
