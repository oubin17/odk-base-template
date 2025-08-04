package com.odk.baseweb.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserProfileApi;
import com.odk.baseutil.request.UserProfileRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* UserProfileController
*   
* @description:    
* @version:        1.0
* @author:         oubin on 2025/8/4
*/
@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserProfileApi userProfileApi;

    public UserProfileController(UserProfileApi userProfileApi) {
        this.userProfileApi = userProfileApi;
    }

    /**
     * 更新用户信息
     *
     * @param userProfileRequest
     * @return
     */
    @PostMapping("/update")
    public ServiceResponse<Boolean> update(@RequestBody @Valid UserProfileRequest userProfileRequest) {
        return userProfileApi.updateUserProfile(userProfileRequest);
    }
}
