package com.odk.basedomain.mapper;

import com.odk.basedomain.dataobject.permission.PermissionDO;
import com.odk.baseutil.dto.permission.PermissionDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * PermissionDomainMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface PermissionDomainMapper {

    PermissionDTO toDTO(PermissionDO permissionDO);

    List<PermissionDTO> toDTO(List<PermissionDO> permissionDO);
}
