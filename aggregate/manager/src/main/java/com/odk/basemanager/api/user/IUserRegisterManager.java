package com.odk.basemanager.api.user;

import com.odk.baseutil.dto.user.UserRegisterDTO;

/**
 * IUserRegisterManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IUserRegisterManager {

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    String registerUser(UserRegisterDTO userRegisterDTO);
}
