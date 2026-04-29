package com.odk.basedomain.repository.privacy;

import com.odk.basedomain.model.privacy.UserPrivacyLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserPrivacyLogRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
public interface UserPrivacyLogRepository extends JpaRepository<UserPrivacyLogDO, String> {
}
