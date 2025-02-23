package com.odk.baseservice.impl.user;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserQueryApi;
import com.odk.baseapi.request.UserQueryRequest;
import com.odk.basemanager.deal.user.UserQueryManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserQueryService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserQueryService extends AbstractApiImpl implements UserQueryApi {

    private UserQueryManager userQueryManager;

    @Override
    public ServiceResponse<UserEntity> queryUserByUserId(String userId) {

        return super.bizProcess(BizScene.USER_QUERY, userId, new ApiCallBack<UserEntity, UserEntity>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.notNull(request, BizErrorCode.PARAM_ILLEGAL, "userId is null.");
            }

            @Override
            protected UserEntity doProcess(Object args) {
                return userQueryManager.queryByUserId(userId);
            }

            @Override
            protected UserEntity convertResult(UserEntity userEntity) {
               return userEntity;
            }

        });
    }

    @Override
    public ServiceResponse<UserEntity> queryCurrentUser() {
        return super.bizProcess(BizScene.USER_QUERY, null, new ApiCallBack<UserEntity, UserEntity>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.isTrue(SessionContext.isLogin(), BizErrorCode.USER_NOT_LOGIN, "用户未登录.");
            }

            @Override
            protected UserEntity doProcess(Object args) {
                return userQueryManager.queryByUserId(SessionContext.getLoginIdAsString());
            }

            @Override
            protected UserEntity convertResult(UserEntity userEntity) {
                return userEntity;
            }

        });
    }

    @Override
    public ServiceResponse<UserEntity> queryUserByLoginId(UserQueryRequest userQueryRequest) {

        return super.bizProcess(BizScene.USER_QUERY, userQueryRequest, new ApiCallBack<UserEntity, UserEntity>() {
            @Override
            protected void checkParams(Object request) {
                UserQueryRequest queryRequest = (UserQueryRequest) request;
                AssertUtil.notNull(queryRequest.getLoginId(), BizErrorCode.PARAM_ILLEGAL, "loginId is null.");
                AssertUtil.notNull(queryRequest.getLoginType(), BizErrorCode.PARAM_ILLEGAL, "loginType is null.");
            }

            @Override
            protected UserEntity doProcess(Object args) {
                return userQueryManager.queryByAccessToken(userQueryRequest.getLoginType(), userQueryRequest.getLoginId());
            }

            @Override
            protected UserEntity convertResult(UserEntity userEntity) {
               return userEntity;
            }

        });
    }

    @Autowired
    public void setUserQueryManager(UserQueryManager userQueryManager) {
        this.userQueryManager = userQueryManager;
    }
}
