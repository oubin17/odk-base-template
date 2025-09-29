package com.odk.baseweb.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserStatusApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserStatusController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@RestController
@RequestMapping("/user/status")
public class UserStatusController {

    private UserStatusApi userStatusApi;

    @PostMapping("/freeze")
    public ServiceResponse<Boolean> freezeUser(@RequestParam("userId") String userId) {
        return userStatusApi.freezeUser(userId);
    }

    @PostMapping("/unfreeze")
    public ServiceResponse<Boolean> unfreezeUser(@RequestParam("userId") String userId) {
        return userStatusApi.unfreezeUser(userId);
    }

    @Autowired
    public void setUserStatusApi(UserStatusApi userStatusApi) {
        this.userStatusApi = userStatusApi;
    }
}
