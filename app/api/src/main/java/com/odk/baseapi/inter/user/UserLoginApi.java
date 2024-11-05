package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.request.UserLoginRequest;
import com.odk.baseapi.response.UserLoginResponse;

/**
 * UserLoginApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
public interface UserLoginApi {


    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest);
}
