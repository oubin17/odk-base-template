package com.odk.basedomain.repository.user;

import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.baseutil.request.UserListQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * UserBaseRepository
 * /**
 * 查询方法：
 * findBy/getBy/queryBy/readBy 后面跟要查询的字段名，用于精确匹配。
 * find/get/query/read 后面跟要查询的字段名，使用条件表达式进行模糊匹配。
 * findAll/getAll 后面不跟字段名，表示查询所有记录。
 * <p>
 * 支持的关键字：
 * And：连接多个查询条件，相当于 SQL 中的 AND。
 * Or：连接多个查询条件，相当于 SQL 中的 OR。
 * Between：用于查询字段在某个范围内的记录。
 * LessThan/LessThanEqual：用于查询字段小于某个值的记录。
 * GreaterThan/GreaterThanEqual：用于查询字段大于某个值的记录。
 * IsNull/IsNotNull：用于查询字段为空或不为空的记录。
 * Like/NotLike：用于模糊查询字段值。
 * OrderBy：用于指定查询结果的排序方式。
 * <p>
 * 删除方法：
 * deleteBy/removeBy 后面跟要查询的字段名，用于精确匹配。
 * delete/remove 后面跟要查询的字段名，使用条件表达式进行模糊匹配。
 * <p>
 * 统计方法：
 * countBy 后面跟要查询的字段名，用于精确匹配。
 * count 后面不跟字段名，表示统计所有记录数。
 * <p>
 * 更新方法：
 * updateBy 后面跟要查询的字段名，用于精确匹配。
 * update 后面跟要查询的字段名，使用条件表达式进行模糊匹配。
 * <p>
 * 支持的关键字：
 * Set：用于设置要更新的字段的值。
 * Where：用于指定更新操作的条件。
 * <p>
 * 部分查询关键字映射示例
 * 关键字                使用示例
 * And	                findByLastnameAndFirstname
 * Or	                findByLastnameOrFirstname
 * Is,Equals	        findByFirstnameIs,findByFirstnameEquals
 * Between	            findByStartDateBetween
 * LessThan	            findByAgeLessThan
 * LessThanEqual	    findByAgeLessThanEqual
 * GreaterThan	        findByAgeGreaterThan
 * GreaterThanEqual	    findByAgeGreaterThanEqual
 * After	            findByStartDateAfter
 * Before	            findByStartDateBefore
 * IsNull	            findByAgeIsNull
 * IsNotNull,NotNull	findByAge(Is)NotNull
 * Like	                findByFirstnameLike
 * NotLike	            findByFirstnameNotLike
 * StartingWith	        findByFirstnameStartingWith
 * EndingWith	        findByFirstnameEndingWith
 * Containing	        findByFirstnameContaining
 * OrderBy	            findByAgeOrderByLastnameDesc
 * Not	                findByLastnameNot
 * In	                findByAgeIn(Collection ages)
 * NotIn	            findByAgeNotIn(Collection age)
 * TRUE	                findByActiveTrue()
 * FALSE	            findByActiveFalse()
 * IgnoreCase	        findByFirstnameIgnoreCase
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface UserBaseRepository extends JpaRepository<UserBaseDO, String> {

    Optional<UserBaseDO> findByIdAndTenantId(String id, String tenantId);

    /**
     * 分页查找
     *
     * @param tenantId
     * @param pageable
     * @return
     */
    Page<UserBaseDO> findByTenantId(String tenantId, Pageable pageable);


    @Query(value = """
                        SELECT
            	T1.*
            FROM
            	t_user_base t1
            	JOIN t_user_profile t2 ON t1.id = t2.user_id
            	JOIN t_user_access_token t3 ON t1.id = t3.user_id
            	AND t3.token_type = :#{#pageRequest.loginType}
            WHERE
            	t1.tenant_id = :tenantId
            	AND (:#{#pageRequest.userName} is null or t2.user_name = :#{#pageRequest.userName})
            	AND (:#{#pageRequest.loginId} is null or T3.token_value = :#{#pageRequest.loginId})
            	"""
            ,
            countQuery = """
                                SELECT
                    	count(*)
                    FROM
                    	t_user_base t1
                    	JOIN t_user_profile t2 ON t1.id = t2.user_id
                    	JOIN t_user_access_token t3 ON t1.id = t3.user_id
                    	AND t3.token_type = :#{#pageRequest.loginType}
                    WHERE
                    	t1.tenant_id = :tenantId
                    	AND (:#{#pageRequest.userName} is null or t2.user_name = :#{#pageRequest.userName})
                    	AND (:#{#pageRequest.loginId} is null or T3.token_value = :#{#pageRequest.loginId})
                    	""",
            nativeQuery = true)
    Page<UserBaseDO> findPageList(@Param("pageRequest") UserListQueryRequest pageRequest, @Param("tenantId") String tenantId, Pageable pageable);
}
