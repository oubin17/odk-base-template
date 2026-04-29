package com.odk.baseapi.inter.privacy;

import com.odk.base.vo.response.ServiceResponse;

/**
 * UserPrivacyApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
public interface UserPrivacyApi {

    ServiceResponse<Boolean> agree(String version);
}
