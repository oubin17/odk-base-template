package com.odk.basedomain.domain;

import com.odk.baseutil.entity.UserBaseEntity;

/**
 * UserBaseDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public interface UserBaseDomain {

    /**
     * 获取用户 id
     *
     * @param userId
     * @return
     */
    UserBaseEntity getUserBaseInfo(String userId);

}
