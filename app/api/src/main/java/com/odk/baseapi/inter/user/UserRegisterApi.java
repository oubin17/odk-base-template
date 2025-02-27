package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.UserRegisterRequest;

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

}
