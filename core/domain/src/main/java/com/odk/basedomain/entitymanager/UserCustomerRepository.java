package com.odk.basedomain.entitymanager;

import com.odk.basedomain.model.permission.UserRoleDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 * UserCustomerRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/12/5
 */
@Repository
public class UserCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据角色码查询角色
     *
     * @param roleCode
     * @return
     */
    public UserRoleDO findByRoleCode(String roleCode) {
        String sql = "SELECT * FROM t_user_role WHERE ROLE_CODE = ?";
        Query query = entityManager.createNativeQuery(sql, UserRoleDO.class);
        query.setParameter(1, roleCode);
        return CustomerRepositoryUtil.getSingleResultSafelyByCatch(query);
    }

}
