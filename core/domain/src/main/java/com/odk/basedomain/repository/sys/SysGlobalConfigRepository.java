package com.odk.basedomain.repository.sys;

import com.odk.basedomain.model.sys.SysGlobalConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SysGlobalConfigRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
public interface SysGlobalConfigRepository extends JpaRepository<SysGlobalConfigDO, String> {

    SysGlobalConfigDO findByConfigKeyAndTenantId(String configKey, String tenantId);
}
