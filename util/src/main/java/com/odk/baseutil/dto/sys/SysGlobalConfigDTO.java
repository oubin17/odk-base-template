package com.odk.baseutil.dto.sys;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * SysGlobalConfigEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@Data
public class SysGlobalConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8093626454866487497L;

    private String configKey;

    private String configValue;

    private String configDesc;
}
