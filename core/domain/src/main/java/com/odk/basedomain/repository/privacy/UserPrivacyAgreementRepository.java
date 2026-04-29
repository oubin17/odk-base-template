package com.odk.basedomain.repository.privacy;

import com.odk.basedomain.model.privacy.UserPrivacyAgreementDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserPrivacyAgreementRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
public interface UserPrivacyAgreementRepository extends JpaRepository<UserPrivacyAgreementDO, String> {

    UserPrivacyAgreementDO findByUserId(String userId);
}
