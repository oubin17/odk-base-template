package com.odk.basedomain.domain;

import com.odk.baseutil.dto.UserRegisterDTO;

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
    Long registerUser(UserRegisterDTO userRegisterDTO);

}
