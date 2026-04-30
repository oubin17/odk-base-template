package com.odk.baseutil.request.sys;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * SysGlobalConfigRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysGlobalConfigRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 8093626454866487497L;

    private String configKey;

    private String configValue;

    private String configDesc;
}
