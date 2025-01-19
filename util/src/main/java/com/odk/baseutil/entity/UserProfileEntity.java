package com.odk.baseutil.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * UserProfileEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/19
 */
@Data
public class UserProfileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1892029127638395554L;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private String birthDay;
}
