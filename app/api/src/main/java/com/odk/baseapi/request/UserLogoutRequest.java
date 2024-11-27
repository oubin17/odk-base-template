package com.odk.baseapi.request;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserLogoutRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLogoutRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 4276293963124220004L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 是否登录态
     */
    private boolean isLogin = true;

}
