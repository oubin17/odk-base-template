package com.odk.baseapi.inter.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;

/**
 * RedissonLockApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
public interface RedissonLockApi {

    ServiceResponse<Boolean> lock(String lockKey);
}
