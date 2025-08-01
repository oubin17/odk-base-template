package com.odk.baseservice.impl.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.RedissonLockApi;
import com.odk.basemanager.nonbusiness.RedissonLockManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.threadpool.AsyncConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RedissonLockService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@Service
public class RedissonLockService extends AbstractApiImpl implements RedissonLockApi {

    private RedissonLockManager redissonLockManager;

    @Override
    public ServiceResponse<Boolean> lock(String lockKey) {
        return super.bizProcess(BizScene.USER_QUERY, lockKey, new AbstractApiImpl.ApiCallBack<Boolean, Boolean>() {

            @Override
            protected Boolean doProcess(Object args) {
                for (int i = 0; i < 50; i++) {
                    AsyncConfig.COMMON_EXECUTOR.execute(() -> redissonLockManager.lock(lockKey));
                }
                return true;
            }

            @Override
            protected Boolean convertResult(Boolean result) {
                return result;
            }

        });
    }

    @Autowired
    public void setRedissonLockManager(RedissonLockManager redissonLockManager) {
        this.redissonLockManager = redissonLockManager;
    }
}
