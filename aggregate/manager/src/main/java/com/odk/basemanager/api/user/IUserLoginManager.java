package com.odk.basemanager.api.user;

import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;

/**
 * IUserLoginManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IUserLoginManager {

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    UserEntity userLogin(UserLoginDTO userLoginDTO);

    /**
     * 用户登出
     *
     * @return
     */
    Boolean userLogout();
}
