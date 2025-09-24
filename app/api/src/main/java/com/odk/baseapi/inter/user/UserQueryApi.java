package com.odk.baseapi.inter.user;

import com.odk.base.vo.request.PageParamRequest;
import com.odk.base.vo.response.PageResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.UserQueryRequest;
import com.odk.baseutil.entity.UserEntity;

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
    ServiceResponse<UserEntity> queryUserByUserId(String userId);

    /**
     * 查找当前登录用户信息
     *
     * @return
     */
    ServiceResponse<UserEntity> queryCurrentUser();


    /**
     * 根据loginId查询对象
     *
     * @param userQueryRequest
     * @return
     */
    ServiceResponse<UserEntity> queryUserByLoginId(UserQueryRequest userQueryRequest);


    /**
     * 查询用户列表
     *
     * @param pageRequest
     * @return
     */
    ServiceResponse<PageResponse<UserEntity>> queryUserPageList(PageParamRequest pageRequest);

}
