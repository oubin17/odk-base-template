package com.odk.basedomain.repository.user;

import com.odk.basedomain.model.user.UserAccessTokenDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserAccessTokenRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface UserAccessTokenRepository extends JpaRepository<UserAccessTokenDO, String> {

    /**
     * 查找手机号是否存在
     *
     * @param tokenType
     * @param tokenValue
     * @param tenantId
     * @return
     */
    UserAccessTokenDO findByTokenTypeAndTokenValueAndTenantId(String tokenType, String tokenValue, String tenantId);

    /**
     * 根据用户id查询
     *
     * @param userId
     * @param tenantId
     * @return
     */
    UserAccessTokenDO findByUserIdAndTenantId(String userId, String tenantId);
}
