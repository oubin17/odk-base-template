package com.odk.baseservice.impl.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.RedissionLockApi;
import com.odk.basemanager.deal.nonbusiness.RedissionLockManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.threadpool.AsyncConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RedissionLockService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@Service
public class RedissionLockService extends AbstractApiImpl implements RedissionLockApi {

    private RedissionLockManager redissionLockManager;

    @Override
    public ServiceResponse<Boolean> lock(String lockKey) {
        return super.bizProcess(BizScene.USER_QUERY, lockKey, new AbstractApiImpl.ApiCallBack<Boolean, Boolean>() {

            @Override
            protected Boolean doProcess(Object args) {
                for (int i = 0; i < 50; i++) {
                    AsyncConfig.COMMON_EXECUTOR.execute(() -> redissionLockManager.lock(lockKey));
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
    public void setRedissionLockManager(RedissionLockManager redissionLockManager) {
        this.redissionLockManager = redissionLockManager;
    }
}
