package com.odk.basemanager.deal.permission;

import com.odk.basedomain.dataobject.permission.PermissionDO;
import com.odk.basedomain.mapper.PermissionDomainMapper;
import com.odk.basedomain.repository.permission.PermissionRepository;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.baseutil.request.role.PermissionQueryRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PermissionManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/2/27
 */
@Slf4j
@Service
@AllArgsConstructor
public class PermissionManager {

    private PermissionRepository permissionRepository;

    private PermissionDomainMapper permissionDomainMapper;

    public Boolean addPermission(PermissionAddRequest permissionAddRequest) {
        return false;
    }

    public List<PermissionDTO> permissionList(PermissionQueryRequest queryRequest) {
        List<PermissionDO> allRolePermission;
        if (StringUtils.isBlank(queryRequest.getRoleId())) {
            //分页查全部
            Pageable pageable = Pageable.ofSize(queryRequest.getPageSize()).withPage(queryRequest.getPageNo() - 1);
            Page<PermissionDO> all = permissionRepository.findAll(pageable);
            allRolePermission = all.toList();
        } else {
            allRolePermission = permissionRepository.findRolePermissionByRoleId(queryRequest.getRoleId());
        }
        return permissionDomainMapper.toDTO(allRolePermission);
    }


}
