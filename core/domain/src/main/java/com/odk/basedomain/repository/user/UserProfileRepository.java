package com.odk.basedomain.repository.user;

import com.odk.basedomain.model.user.UserProfileDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserProfileRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
public interface UserProfileRepository extends JpaRepository<UserProfileDO, String> {

    /**
     * 根据用户id查询
     *
     * @param userId
     * @param tenantId
     * @return
     */
    UserProfileDO findByUserIdAndTenantId(String userId, String tenantId);
}
