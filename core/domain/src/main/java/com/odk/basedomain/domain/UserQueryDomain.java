package com.odk.basedomain.domain;

import com.odk.baseutil.entity.UserEntity;

/**
 * UserQueryDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
public interface UserQueryDomain {

    /**
     * 根据用户id查找
     *
     * @param userId
     * @return
     */
    UserEntity queryByUserId(String userId);

    /**
     * 根据用户id查找，如果不存在，抛异常
     *
     * @param userId
     * @return
     */
    UserEntity queryByUserIdAndCheck(String userId);

    /**
     * 从 session 中获取用户信息
     *
     * @return
     */
    UserEntity queryBySession();


    UserEntity queryBySessionAndCheck();

    /**
     * 根据登录id查找
     *
     * @param
     * @param tokenValue
     * @return
     */
    UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue);

}
