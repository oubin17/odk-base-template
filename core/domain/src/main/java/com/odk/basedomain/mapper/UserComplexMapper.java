package com.odk.basedomain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.baseutil.request.UserListQueryRequest;
import org.apache.ibatis.annotations.Param;

/**
 * UserComplexMapper
 *
 * @description: 用户复杂查询Mapper接口
 * @version: 1.0
 * @author: oubin on 2025/10/17
 */
public interface UserComplexMapper extends BaseMapper<UserBaseDO> {

    /**
     * 根据条件分页查询用户列表
     *
     * @param page
     * @param queryRequest
     * @return
     */
    IPage<UserBaseDO> selectUserPage(IPage<UserBaseDO> page, @Param("query") UserListQueryRequest queryRequest, @Param("tenantId") String tenantId);
}