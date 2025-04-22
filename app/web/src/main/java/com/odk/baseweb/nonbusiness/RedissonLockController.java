package com.odk.baseweb.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.RedissonLockApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedissonLockController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@RestController
@RequestMapping("/redisson")
public class RedissonLockController {

    private RedissonLockApi redissonLockApi;

    @GetMapping("/lock")
    public ServiceResponse<Boolean> addPermission(@RequestParam("lockKey") String lockKey) {
        return this.redissonLockApi.lock(lockKey);
    }

    @Autowired
    public void setRedissonLockApi(RedissonLockApi redissonLockApi) {
        this.redissonLockApi = redissonLockApi;
    }
}
