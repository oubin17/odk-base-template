package com.odk.baseservice.impl.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserQueryApi;
import com.odk.baseapi.request.UserQueryRequest;
import com.odk.baseapi.response.UserQueryResponse;
import com.odk.basemanager.entity.UserEntity;
import com.odk.basemanager.deal.user.UserQueryManager;
import com.odk.baseservice.template.AbstractApiImpl;
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
        UserEntity userEntity = userQueryManager.queryByUserId(userId);
        if (null == userEntity) {
            return ServiceResponse.valueOfSuccess();
        }
        UserQueryResponse userQueryResponse = new UserQueryResponse();
        BeanUtils.copyProperties(userEntity, userQueryResponse);
        return ServiceResponse.valueOfSuccess(userQueryResponse);
    }

    @Override
    public ServiceResponse<UserQueryResponse> queryUserByLoginId(UserQueryRequest userQueryRequest) {
        UserEntity userEntity = userQueryManager.queryByAccessToken(userQueryRequest.getLoginType(), userQueryRequest.getLoginId());
        if (null == userEntity) {
            return ServiceResponse.valueOfSuccess();
        }
        UserQueryResponse userQueryResponse = new UserQueryResponse();
        BeanUtils.copyProperties(userEntity, userQueryResponse);
        return ServiceResponse.valueOfSuccess(userQueryResponse);
    }

    @Autowired
    public void setUserQueryManager(UserQueryManager userQueryManager) {
        this.userQueryManager = userQueryManager;
    }
}
