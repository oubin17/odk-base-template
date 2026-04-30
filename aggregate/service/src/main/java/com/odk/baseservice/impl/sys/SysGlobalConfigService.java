package com.odk.baseservice.impl.sys;

import com.odk.base.context.TenantIdContext;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.sys.SysGlobalConfigApi;
import com.odk.basedomain.convert.SysGlobalConfigConvert;
import com.odk.basedomain.model.sys.SysGlobalConfigDO;
import com.odk.basedomain.repository.sys.SysGlobalConfigRepository;
import com.odk.baseutil.dto.sys.SysGlobalConfigDTO;
import com.odk.baseutil.request.sys.SysGlobalConfigRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * SysGlobalConfigService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@Service
@AllArgsConstructor
public class SysGlobalConfigService implements SysGlobalConfigApi {

    private SysGlobalConfigConvert sysGlobalConfigConvert;

    private SysGlobalConfigRepository sysGlobalConfigRepository;

    @Override
    public ServiceResponse<SysGlobalConfigDTO> getGlobalConfig(String configKey) {
        SysGlobalConfigDO sysGlobalConfigDO = sysGlobalConfigRepository.findByConfigKeyAndTenantId(configKey, TenantIdContext.getTenantId());
        if (null != sysGlobalConfigDO) {
            return ServiceResponse.valueOfSuccess(sysGlobalConfigConvert.convertToDTO(sysGlobalConfigDO));
        }
        return ServiceResponse.valueOfSuccess(null);
    }

    @Override
    public ServiceResponse<Void> updateGlobalConfig(SysGlobalConfigRequest sysGlobalConfigRequest) {
        SysGlobalConfigDO sysGlobalConfigDO = sysGlobalConfigRepository.findByConfigKeyAndTenantId(sysGlobalConfigRequest.getConfigKey(), TenantIdContext.getTenantId());
        if (null != sysGlobalConfigDO) {
            sysGlobalConfigDO.setConfigValue(sysGlobalConfigRequest.getConfigValue());
            sysGlobalConfigDO.setConfigDesc(sysGlobalConfigRequest.getConfigDesc());
            sysGlobalConfigRepository.save(sysGlobalConfigDO);
            return ServiceResponse.valueOfSuccess();
        }
        return ServiceResponse.valueOfSuccess();
    }

    @Override
    public ServiceResponse<Void> addGlobalConfig(SysGlobalConfigRequest sysGlobalConfigRequest) {
        SysGlobalConfigDO sysGlobalConfigDO = sysGlobalConfigRepository.findByConfigKeyAndTenantId(sysGlobalConfigRequest.getConfigKey(), TenantIdContext.getTenantId());

        if (null != sysGlobalConfigDO) {
            return ServiceResponse.valueOfSuccess();
        }
        sysGlobalConfigDO = sysGlobalConfigConvert.convertToDO(sysGlobalConfigRequest);
        sysGlobalConfigDO.setTenantId(TenantIdContext.getTenantId());
        sysGlobalConfigRepository.save(sysGlobalConfigDO);
        return ServiceResponse.valueOfSuccess();
    }
}
