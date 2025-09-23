package com.odk.basedomain.domain;

import com.odk.base.vo.request.PageParamRequest;
import com.odk.base.vo.response.PageResponse;
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

    /**
     * 分页查找
     *
     * @param pageParamRequest
     * @return
     */
    PageResponse<UserEntity> queryUserList(PageParamRequest pageParamRequest);

}
