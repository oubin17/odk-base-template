package com.odk.basedomain.domain;

import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.entity.UserEntity;

/**
 * UserDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
public interface UserDomain {

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    String registerUser(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    UserEntity userLogin(UserLoginDTO userLoginDTO);

}
