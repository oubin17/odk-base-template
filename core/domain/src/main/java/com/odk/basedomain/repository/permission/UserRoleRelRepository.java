package com.odk.basedomain.repository.permission;

import com.odk.basedomain.domain.permission.UserRoleRelDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRoleRelRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
public interface UserRoleRelRepository extends JpaRepository<UserRoleRelDO, Long> {
}
