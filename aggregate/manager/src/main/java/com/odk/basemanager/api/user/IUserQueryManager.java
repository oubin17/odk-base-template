package com.odk.basemanager.api.user;

import com.odk.base.vo.response.PageResponse;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.request.UserListQueryRequest;

/**
 * IUserQueryManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IUserQueryManager {

    UserEntity queryByUserId(String userId);

    UserEntity queryBySession();

    UserEntity queryByAccessToken(String tokenType, String tokenValue);

    PageResponse<UserEntity> queryUserPageList(UserListQueryRequest pageRequest);
}
