package com.odk.basedomain.entitymanager;

import com.odk.base.context.TenantIdContext;
import com.odk.base.vo.request.PageParamRequest;
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
     * 用户列表
     *
     * @param pageParamRequest
     * @return
     */
    public Page<String> queryUserPageList(PageParamRequest pageParamRequest) {
        UserListQueryRequest request = (UserListQueryRequest) pageParamRequest;

        // 构建主查询SQL
        String mainSql = buildUserQuerySql(request, false);
        Query query = entityManager.createNativeQuery(mainSql);
        setQueryParameters(query, request);

        // 设置分页参数
        query.setParameter("offset", request.getPage() * request.getSize());
        query.setParameter("pageSize", request.getSize());

        List<String> results = query.getResultList();

        // 构建count查询SQL
        String countSql = buildUserQuerySql(request, true);
        Query countQuery = entityManager.createNativeQuery(countSql);
        setQueryParameters(countQuery, request);

        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(results, PageRequest.of(request.getPage(), request.getSize()), total);
    }

    private String buildUserQuerySql(UserListQueryRequest request, boolean isCount) {
        StringBuilder sqlBuilder = new StringBuilder();

        if (isCount) {
            sqlBuilder.append("SELECT COUNT(*) ");
        } else {
            sqlBuilder.append("SELECT t1.id ");
        }

        sqlBuilder.append("FROM t_user_base t1 ")
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

        if (!isCount) {
            sqlBuilder.append("ORDER BY t1.create_time DESC ")
                    .append("LIMIT :offset, :pageSize");
        }

        return sqlBuilder.toString();
    }

    private void setQueryParameters(Query query, UserListQueryRequest request) {
        query.setParameter("tokenType", request.getLoginType());
        query.setParameter("tenantId", TenantIdContext.getTenantId());

        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            query.setParameter("userName", "%" + request.getUserName() + "%");
        }

        if (request.getLoginId() != null && !request.getLoginId().isEmpty()) {
            query.setParameter("tokenValue", "%" + request.getLoginId() + "%");
        }
    }

}
