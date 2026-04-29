package com.odk.basemanager.impl.privacy;

import com.odk.basedomain.model.privacy.UserPrivacyAgreementDO;
import com.odk.basedomain.model.privacy.UserPrivacyLogDO;
import com.odk.basedomain.repository.privacy.UserPrivacyAgreementRepository;
import com.odk.basedomain.repository.privacy.UserPrivacyLogRepository;
import com.odk.basemanager.api.privacy.IUserPrivacyManager;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserPrivacyManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserPrivacyManager implements IUserPrivacyManager {


    private UserPrivacyAgreementRepository userPrivacyAgreementRepository;

    private UserPrivacyLogRepository userPrivacyLogRepository;


    @Override
    public void registerAgree(String userId, String version) {
        firstAgree(userId, version);
    }


    @Override
    public boolean agree(String version) {

        UserPrivacyAgreementDO byUserId = userPrivacyAgreementRepository.findByUserId(SessionContext.getLoginIdWithCheck());
        if (byUserId == null) {
            UserPrivacyAgreementDO userPrivacyAgreementDO = new UserPrivacyAgreementDO();
            userPrivacyAgreementDO.setUserId(SessionContext.getLoginIdWithCheck());
            userPrivacyAgreementDO.setAgreePrivacy(1);
            userPrivacyAgreementDO.setAgreePrivacyVersion(version);
            userPrivacyAgreementDO.setUserAgreement(1);
            userPrivacyAgreementDO.setUserAgreementVersion(version);
            userPrivacyAgreementRepository.save(userPrivacyAgreementDO);
        } else {
            byUserId.setAgreePrivacyVersion(version);
            byUserId.setUserAgreementVersion(version);
            userPrivacyAgreementRepository.save(byUserId);
        }
        UserPrivacyLogDO userPrivacyLogDO = new UserPrivacyLogDO();
        userPrivacyLogDO.setUserId(SessionContext.getLoginIdWithCheck());
        userPrivacyLogDO.setOperation(1);
        userPrivacyLogDO.setAgreePrivacyVersion(version);
        userPrivacyLogRepository.save(userPrivacyLogDO);

        UserPrivacyLogDO userPrivacyLogDO2 = new UserPrivacyLogDO();
        userPrivacyLogDO2.setUserId(SessionContext.getLoginIdWithCheck());
        userPrivacyLogDO2.setOperation(1);
        userPrivacyLogDO2.setUserAgreementVersion(version);
        userPrivacyLogRepository.save(userPrivacyLogDO2);

        return true;
    }

    @Override
    public boolean revoke(String version) {
        return false;
    }


    private void firstAgree(String userId, String version) {
        UserPrivacyAgreementDO userPrivacyAgreementDO = new UserPrivacyAgreementDO();
        userPrivacyAgreementDO.setUserId(userId);
        userPrivacyAgreementDO.setAgreePrivacy(1);
        userPrivacyAgreementDO.setAgreePrivacyVersion(version);
        userPrivacyAgreementDO.setUserAgreement(1);
        userPrivacyAgreementDO.setUserAgreementVersion(version);
        userPrivacyAgreementRepository.save(userPrivacyAgreementDO);

        UserPrivacyLogDO userPrivacyLogDO = new UserPrivacyLogDO();
        userPrivacyLogDO.setUserId(userId);
        userPrivacyLogDO.setOperation(1);
        userPrivacyLogDO.setAgreePrivacyVersion(version);
        userPrivacyLogRepository.save(userPrivacyLogDO);

        UserPrivacyLogDO userPrivacyLogDO2 = new UserPrivacyLogDO();
        userPrivacyLogDO2.setUserId(userId);
        userPrivacyLogDO2.setOperation(1);
        userPrivacyLogDO2.setUserAgreementVersion(version);
        userPrivacyLogRepository.save(userPrivacyLogDO2);
    }
}
