package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.request.UserQueryRequest;
import com.odk.baseapi.response.UserQueryResponse;

/**
 * UserQueryApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
public interface UserQueryApi {


    /**
     * 根据userId查询对象
     *
     * @param userId
     * @return
     */
    ServiceResponse<UserQueryResponse> queryUserByUserId(String userId);

    /**
     * 根据loginId查询对象
     *
     * @param userQueryRequest
     * @return
     */
    ServiceResponse<UserQueryResponse> queryUserByLoginId(UserQueryRequest userQueryRequest);

}
