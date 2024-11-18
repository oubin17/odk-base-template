package com.odk.basedomain.interceptor;

import lombok.Data;

/**
 * AuditorInfo
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/18
 */
@Data
public class AuditorInfo {

    private String createdBy;

    private String tenantId;

    public AuditorInfo(String createdBy, String tenantId) {
        this.createdBy = createdBy;
        this.tenantId = tenantId;
    }
}
