package com.odk.baseservice.impl.privacy;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.privacy.UserPrivacyApi;
import com.odk.basemanager.api.privacy.IUserPrivacyManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.enums.BizScene;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserPrivacyService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserPrivacyService implements UserPrivacyApi {

    private IUserPrivacyManager userPrivacyManager;

    @Override
    @BizProcess(bizScene = BizScene.PRIVACY_AGREE)
    public ServiceResponse<Boolean> agree(String version) {
        return ServiceResponse.valueOfSuccess(userPrivacyManager.agree(version));
    }
}
