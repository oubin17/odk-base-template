package com.odk.baseapi.inter.sys;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.dto.sys.SysGlobalConfigDTO;
import com.odk.baseutil.request.sys.SysGlobalConfigRequest;

/**
 * SysGlobalConfigApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
public interface SysGlobalConfigApi {

    ServiceResponse<SysGlobalConfigDTO> getGlobalConfig(String configKey);

    ServiceResponse<Void> updateGlobalConfig(SysGlobalConfigRequest sysGlobalConfigRequest);

    ServiceResponse<Void> addGlobalConfig(SysGlobalConfigRequest sysGlobalConfigRequest);
}
