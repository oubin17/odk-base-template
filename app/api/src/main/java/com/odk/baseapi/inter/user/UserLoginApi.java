package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.UserLoginRequest;
import com.odk.baseutil.response.UserLoginResponse;

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

    /**
     * 注销登录
     *
     * @return
     */
    ServiceResponse<Boolean> userLogout();

}
