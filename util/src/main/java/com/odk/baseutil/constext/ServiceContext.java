package com.odk.baseutil.constext;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ServiceContext
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@Data
public class ServiceContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 8770851789533803554L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户ID
     */
    private String tenantId;
}
