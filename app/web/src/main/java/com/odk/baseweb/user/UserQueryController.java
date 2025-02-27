package com.odk.baseweb.user;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserQueryApi;
import com.odk.baseutil.request.UserQueryRequest;
import com.odk.baseutil.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserQueryController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@RestController
@RequestMapping("/user/query")
public class UserQueryController {

    private UserQueryApi userQueryApi;

    /**
     * 根据用户id查找（管理员权限）
     *
     * @param userId {@link com.odk.baseutil.enums.InnerRoleEnum}
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @GetMapping("/userId")
    public ServiceResponse<UserEntity> queryUserByUserId(@RequestParam("userId") String userId) {
        return userQueryApi.queryUserByUserId(userId);
    }

    /**
     * 查找当前登录用户
     *
     * @return
     */
    @GetMapping()
    public ServiceResponse<UserEntity> queryCurrentUser() {
        return userQueryApi.queryCurrentUser();
    }

    @GetMapping("/loginId")
    public ServiceResponse<UserEntity> queryUserByLoginId(@RequestParam("loginId") String loginId, @RequestParam("loginType") String loginType) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setLoginId(loginId);
        userQueryRequest.setLoginType(loginType);
        return userQueryApi.queryUserByLoginId(userQueryRequest);
    }


    @Autowired
    public void setUserQueryApi(UserQueryApi userQueryApi) {
        this.userQueryApi = userQueryApi;
    }

}
