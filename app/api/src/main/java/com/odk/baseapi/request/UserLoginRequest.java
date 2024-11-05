package com.odk.baseapi.request;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserLoginRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -5548611718928196243L;

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 密码类型
     */
    private String identifyType;

    /**
     * 密码
     */
    private String identifyValue;
}
