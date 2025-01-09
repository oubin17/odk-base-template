package com.odk.baseservice.impl.user;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserQueryApi;
import com.odk.baseapi.request.UserQueryRequest;
import com.odk.baseapi.response.UserQueryResponse;
import com.odk.basemanager.deal.user.UserQueryManager;
import com.odk.basedomain.entity.UserEntity;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import org.springframework.beans.BeanUtils;
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
    public ServiceResponse<UserQueryResponse> queryUserByUserId(String userId) {

        return super.queryProcess(BizScene.USER_QUERY, userId, new QueryApiCallBack<UserEntity, UserQueryResponse>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.notNull(request, BizErrorCode.PARAM_ILLEGAL, "userId is null.");
            }

            @Override
            protected UserEntity doProcess(Object args) {
                return userQueryManager.queryByUserId(userId);
            }

            @Override
            protected UserQueryResponse convertResult(UserEntity userEntity) {
                if (null == userEntity) {
                    return null;
                }
                UserQueryResponse userQueryResponse = new UserQueryResponse();
                BeanUtils.copyProperties(userEntity, userQueryResponse);
                return userQueryResponse;
            }

        });
    }

    @Override
    public ServiceResponse<UserQueryResponse> queryCurrentUser() {
        return super.queryProcess(BizScene.USER_QUERY, null, new QueryApiCallBack<UserEntity, UserQueryResponse>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.isTrue(StpUtil.isLogin(), BizErrorCode.USER_NOT_LOGIN, "用户未登录.");
            }

            @Override
            protected UserEntity doProcess(Object args) {
                return userQueryManager.queryByUserId(StpUtil.getLoginIdAsString());
            }

            @Override
            protected UserQueryResponse convertResult(UserEntity userEntity) {
                if (null == userEntity) {
                    return null;
                }
                UserQueryResponse userQueryResponse = new UserQueryResponse();
                BeanUtils.copyProperties(userEntity, userQueryResponse);
                return userQueryResponse;
            }

        });
    }

    @Override
    public ServiceResponse<UserQueryResponse> queryUserByLoginId(UserQueryRequest userQueryRequest) {

        return super.queryProcess(BizScene.USER_QUERY, userQueryRequest, new QueryApiCallBack<UserEntity, UserQueryResponse>() {
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
            protected UserQueryResponse convertResult(UserEntity userEntity) {
                if (null == userEntity) {
                    return null;
                }
                UserQueryResponse userQueryResponse = new UserQueryResponse();
                BeanUtils.copyProperties(userEntity, userQueryResponse);
                return userQueryResponse;
            }

        });
    }

    @Autowired
    public void setUserQueryManager(UserQueryManager userQueryManager) {
        this.userQueryManager = userQueryManager;
    }
}
