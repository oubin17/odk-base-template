package com.odk.basedomain.repository.sys;

import com.odk.basedomain.model.sys.SysLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SysLogRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
public interface SysLogRepository extends JpaRepository<SysLogDO, String> {
}
