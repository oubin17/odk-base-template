package com.odk.basedomain.convert;

import com.odk.basedomain.model.sys.SysGlobalConfigDO;
import com.odk.baseutil.dto.sys.SysGlobalConfigDTO;
import com.odk.baseutil.request.sys.SysGlobalConfigRequest;
import org.mapstruct.Mapper;

/**
 * SysGlobalConfigConvert
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@Mapper(componentModel = "spring")
public interface SysGlobalConfigConvert {

    SysGlobalConfigDTO convertToDTO(SysGlobalConfigDO sysGlobalConfigDO);

    SysGlobalConfigDO convertToDO(SysGlobalConfigDTO sysGlobalConfigDTO);

    SysGlobalConfigDO convertToDO(SysGlobalConfigRequest sysGlobalConfigRequest);
}
