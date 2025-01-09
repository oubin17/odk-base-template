package com.odk.baseutil.dto;

import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserRegisterDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends DTO {

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码类型
     *
     */
    private String identifyType;

    /**
     * 密码
     */
    private String identifyValue;


    /**
     * 用户ID
     */
    private String userId;

}
