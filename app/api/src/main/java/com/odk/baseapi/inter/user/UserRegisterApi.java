package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.UserRegisterNoAuthRequest;
import com.odk.baseutil.request.UserRegisterRequest;
import com.odk.baseutil.response.UserLoginResponse;

/**
 * UserRegisterApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
public interface UserRegisterApi {

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    ServiceResponse<String> userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 注册后直接登录
     *
     * @param userRegisterRequest
     * @return
     */
    ServiceResponse<UserLoginResponse> loginAfterRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户注册：密码
     *
     * @param noAuthRequest
     * @return
     */
    ServiceResponse<String> userRegisterNoAuth(UserRegisterNoAuthRequest noAuthRequest);
}
