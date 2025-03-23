package com.odk.basedomain.repository.permission;

import com.odk.basedomain.dataobject.permission.UserRoleRelDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRoleRelRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
public interface UserRoleRelRepository extends JpaRepository<UserRoleRelDO, String> {


    /**
     * 根据id查找
     *
     * @param userId
     * @param roleId
     * @return
     */
    UserRoleRelDO findByUserIdAndRoleId(String userId, String roleId);
}
