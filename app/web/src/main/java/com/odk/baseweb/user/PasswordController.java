package com.odk.baseweb.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.PasswordApi;
import com.odk.baseutil.request.password.PasswordSetRequest;
import com.odk.baseutil.request.password.PasswordUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * PasswordController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
@CrossOrigin
@RestController
@RequestMapping("/user/password")
public class PasswordController {

    private PasswordApi passwordApi;


    /**
     * 设置密码
     *
     */
    @PostMapping("/existed")
    ServiceResponse<Boolean> checkExisted(@RequestBody PasswordSetRequest passwordSetRequest) {
        return passwordApi.checkExisted(passwordSetRequest);
    }


    /**
     * 设置密码
     *
     */
    @PostMapping("/set")
    ServiceResponse<Boolean> setPassword(@RequestBody PasswordSetRequest passwordSetRequest) {
        return passwordApi.setPassword(passwordSetRequest);
    }

    /**
     * 重置密码
     *
     */
    @PostMapping("/reset")
    ServiceResponse<Boolean> reSetPassword(@RequestBody PasswordSetRequest passwordSetRequest) {
        return passwordApi.reSetPassword(passwordSetRequest);
    }

    /**
     * 更新密码
     *
     */
    @PostMapping("/update")
    ServiceResponse<Boolean> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        return passwordApi.passwordUpdate(passwordUpdateRequest);
    }

    @Autowired
    public void setPasswordApi(PasswordApi passwordApi) {
        this.passwordApi = passwordApi;
    }
}
