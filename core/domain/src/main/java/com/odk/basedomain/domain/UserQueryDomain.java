package com.odk.basedomain.domain;

import com.odk.basedomain.domain.criteria.UserListQueryCriteria;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.baseutil.entity.UserEntity;

import java.util.List;

/**
 * UserQueryDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
public interface UserQueryDomain {

    /**
     * 统一用户查询接口
     *
     * @param criteria
     * @return
     */
    UserEntity queryUser(UserQueryCriteria criteria);

    /**
     * 批量用户查询接口
     *
     * @param criteria
     * @return
     */
    List<UserEntity> queryUserList(UserListQueryCriteria criteria);

//    /**
//     * 根据用户id查找
//     *
//     * @param userId
//     * @return
//     */
//    UserEntity queryByUserId(String userId);
//
//    /**
//     * 根据用户id查找，如果不存在，抛异常
//     *
//     * @param userId
//     * @return
//     */
//    UserEntity queryByUserIdAndCheck(String userId);
//
//    /**
//     * 从 session 中获取用户信息
//     *
//     * @return
//     */
//    UserEntity queryBySession();
//
//
//    UserEntity queryBySessionAndCheck();
//
//    /**
//     * 根据登录id查找
//     *
//     * @param
//     * @param tokenValue
//     * @return
//     */
//    UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue);

}
