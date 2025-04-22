package com.odk.baseweb.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.RedissionLockApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedissionLockController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@RestController
@RequestMapping("/redission")
public class RedissionLockController {

    private RedissionLockApi redissionLockApi;

    @GetMapping("/lock")
    public ServiceResponse<Boolean> addPermission(@RequestParam("lockKey") String lockKey) {
        return this.redissionLockApi.lock(lockKey);
    }

    @Autowired
    public void setRedissionLockApi(RedissionLockApi redissionLockApi) {
        this.redissionLockApi = redissionLockApi;
    }
}
