package com.odk.baseweb.privacy;

import com.odk.baseapi.inter.privacy.UserPrivacyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserPrivacyController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
@RestController
@RequestMapping("/user/privacy")
public class UserPrivacyController {
    private UserPrivacyApi userPrivacyApi;


//
//    /**
//     * 设置密码
//     *
//     */
//    @PostMapping("/existed")
//    ServiceResponse<Boolean> checkExisted(@RequestBody PasswordSetRequest passwordSetRequest) {
//        return passwordApi.checkExisted(passwordSetRequest);
//    }

    @Autowired
    public void setUserPrivacyApi(UserPrivacyApi userPrivacyApi) {
        this.userPrivacyApi = userPrivacyApi;
    }
}
