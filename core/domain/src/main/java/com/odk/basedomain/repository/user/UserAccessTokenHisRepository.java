package com.odk.basedomain.repository.user;

import com.odk.basedomain.model.user.UserAccessTokenHisDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserAccessTokenHisRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/26
 */
public interface UserAccessTokenHisRepository extends JpaRepository<UserAccessTokenHisDO, String> {
}
