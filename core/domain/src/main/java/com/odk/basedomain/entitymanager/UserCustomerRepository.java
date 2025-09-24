package com.odk.basedomain.entitymanager;

import com.odk.base.context.TenantIdContext;
import com.odk.base.vo.request.PageParamRequest;
import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.baseutil.request.UserListQueryRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Page<String> queryUserPageList(PageParamRequest pageParamRequest) {
        UserListQueryRequest request = (UserListQueryRequest) pageParamRequest;

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT t1.id ")
                .append("FROM t_user_base t1 ")
                .append("JOIN t_user_profile t2 ON t1.id = t2.user_id ")
                .append("JOIN t_user_access_token t3 ON t1.id = t3.user_id AND t3.token_type = :tokenType ")
                .append("WHERE t1.tenant_id = :tenantId ");

        // 动态添加条件
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            sqlBuilder.append("AND t2.user_name LIKE :userName ");
        }

        if (request.getLoginId() != null && !request.getLoginId().isEmpty()) {
            sqlBuilder.append("AND t3.token_value LIKE :tokenValue ");
        }

        sqlBuilder.append("ORDER BY t1.create_time DESC ")
                .append("LIMIT :offset, :pageSize");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("tokenType", request.getLoginType());
        query.setParameter("tenantId", TenantIdContext.getTenantId());

        // 只有在参数非空时才设置参数
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            query.setParameter("userName", "%" + request.getUserName() + "%");
        }

        if (request.getLoginId() != null && !request.getLoginId().isEmpty()) {
            query.setParameter("tokenValue", "%" + request.getLoginId() + "%");
        }

        query.setParameter("offset", request.getPage() * request.getSize());
        query.setParameter("pageSize", request.getSize());

        // 执行查询并返回结果
        List<String> results = query.getResultList();

        // 查询总数的SQL

        // 查询总数的SQL也需要相应修改
        StringBuilder countSqlBuilder = new StringBuilder();
        countSqlBuilder.append("SELECT COUNT(*) ")
                .append("FROM t_user_base t1 ")
                .append("JOIN t_user_profile t2 ON t1.id = t2.user_id ")
                .append("JOIN t_user_access_token t3 ON t1.id = t3.user_id AND t3.token_type = :tokenType ")
                .append("WHERE t1.tenant_id = :tenantId ");

        // 动态添加条件
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            countSqlBuilder.append("AND t2.user_name LIKE :userName ");
        }

        if (request.getLoginId() != null && !request.getLoginId().isEmpty()) {
            countSqlBuilder.append("AND t3.token_value LIKE :tokenValue ");
        }

        Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());
        countQuery.setParameter("tokenType", request.getLoginType());
        countQuery.setParameter("tenantId", TenantIdContext.getTenantId());

        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            countQuery.setParameter("userName", "%" + request.getUserName() + "%");
        }

        if (request.getLoginId() != null && !request.getLoginId().isEmpty()) {
            countQuery.setParameter("tokenValue", "%" + request.getLoginId() + "%");
        }

        // 获取总数
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // 返回分页结果
        return new PageImpl<>(results, PageRequest.of(request.getPage(), request.getSize()), total);
    }

}
