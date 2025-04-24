package com.odk.baseapi.inter.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;

/**
 * EncodeApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
public interface EncodeApi {

    ServiceResponse<String> publicKeyEncode(String rawData);
}
