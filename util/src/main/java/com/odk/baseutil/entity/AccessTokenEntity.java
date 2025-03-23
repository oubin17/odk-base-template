package com.odk.baseutil.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AccessTokenEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/19
 */
@Data
public class AccessTokenEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1548527760145329039L;

    /**
     * token 类型
     * {@link com.odk.base.enums.user.TokenTypeEnum}
     */
    private String tokenType;

    /**
     * token值
     */
    private String tokenValue;
}
