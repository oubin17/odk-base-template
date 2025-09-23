package com.odk.basedomain.domain;

import com.odk.baseutil.entity.AccessTokenEntity;

/**
 * AccessTokenDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public interface AccessTokenDomain {

    /**
     * 获取用户登录账号
     *
     * @param userId
     * @return
     */
    AccessTokenEntity getAccessToken(String userId);
}
