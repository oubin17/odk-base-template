package com.odk.baseservice.impl.user;

import com.odk.base.vo.response.PageResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserQueryApi;
import com.odk.basemanager.api.user.IUserQueryManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.UserListQueryRequest;
import com.odk.baseutil.request.UserQueryRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserQueryService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
@AllArgsConstructor
public class UserQueryService implements UserQueryApi {

    private IUserQueryManager userQueryManager;

    @Override
    @BizProcess(bizScene = BizScene.USER_QUERY)
    public ServiceResponse<UserEntity> queryUserByUserId(String userId) {
        return ServiceResponse.valueOfSuccess(userQueryManager.queryByUserId(userId));
    }

    @Override
    @BizProcess(bizScene = BizScene.USER_QUERY)
    public ServiceResponse<UserEntity> queryCurrentUser() {
        return ServiceResponse.valueOfSuccess(userQueryManager.queryBySession());
    }

    @Override
    @BizProcess(bizScene = BizScene.USER_QUERY)
    public ServiceResponse<UserEntity> queryUserByLoginId(UserQueryRequest userQueryRequest) {
        return ServiceResponse.valueOfSuccess(userQueryManager.queryByAccessToken(userQueryRequest.getLoginType(), userQueryRequest.getLoginId()));
    }

    @Override
    @BizProcess(bizScene = BizScene.USER_LIST_QUERY)
    public ServiceResponse<PageResponse<UserEntity>> queryUserPageList(UserListQueryRequest pageRequest) {
        return ServiceResponse.valueOfSuccess(userQueryManager.queryUserPageList(pageRequest));
    }

}
