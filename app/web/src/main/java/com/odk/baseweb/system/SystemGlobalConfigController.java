package com.odk.baseweb.system;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.sys.SysGlobalConfigApi;
import com.odk.baseutil.dto.sys.SysGlobalConfigDTO;
import com.odk.baseutil.request.sys.SysGlobalConfigRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemGlobalConfigController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@RestController
@RequestMapping("/sys/global/config")
public class SystemGlobalConfigController {

    private SysGlobalConfigApi sysGlobalConfigApi;

    @PostMapping("/add")
    public ServiceResponse<Void> addGlobalConfig(@RequestBody SysGlobalConfigRequest sysGlobalConfigRequest) {
        sysGlobalConfigApi.addGlobalConfig(sysGlobalConfigRequest);
        return ServiceResponse.valueOfSuccess();
    }

    @PostMapping("/update")
    public ServiceResponse<Void> updateGlobalConfig(@RequestBody SysGlobalConfigRequest sysGlobalConfigRequest) {
        sysGlobalConfigApi.updateGlobalConfig(sysGlobalConfigRequest);
        return ServiceResponse.valueOfSuccess();
    }

    @PostMapping("/get")
    public ServiceResponse<SysGlobalConfigDTO> getGlobalConfig(String configKey) {
        return sysGlobalConfigApi.getGlobalConfig(configKey);
    }

    @Autowired
    public void setSysGlobalConfigApi(SysGlobalConfigApi sysGlobalConfigApi) {
        this.sysGlobalConfigApi = sysGlobalConfigApi;
    }
}
