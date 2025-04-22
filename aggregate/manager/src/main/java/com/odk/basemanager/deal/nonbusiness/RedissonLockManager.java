package com.odk.basemanager.deal.nonbusiness;

import com.odk.redisspringbootstarter.DistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RedissonLockManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@Slf4j
@Service
public class RedissonLockManager {

    private DistributedLockService distributedLockService;

    private Integer count = 0;


    public void lock(String lockKey) {
        boolean acquired = distributedLockService.tryLock(lockKey, 2, 3);
        if (acquired) {
            log.debug("获取锁成功 {} [thread: {}]", lockKey, Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
                count ++;
                log.info("lockKey:{}, count:{}", lockKey, count);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                distributedLockService.unlock(lockKey);
            }
        } else {
            log.error("获取锁失败 {} [thread: {}]", lockKey, Thread.currentThread().getName());
        }
    }

    @Autowired
    public void setDistributedLockService(DistributedLockService distributedLockService) {
        this.distributedLockService = distributedLockService;
    }
}
