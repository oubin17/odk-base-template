package com.odk.basedomain.domain;

import com.odk.basedomain.entity.UserEntity;
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

    /**
     * 根据用户id查找
     *
     * @param userId
     * @return
     */
    UserEntity queryByUserId(Long userId);

    /**
     * 根据用户id查找，如果不存在，抛异常
     *
     * @param userId
     * @return
     */
    UserEntity queryByUserIdAndCheck(Long userId);

}
