package com.odk.baseapi.request.role;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * RoleAddRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleAddRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 3836916230018832242L;

    /**
     * 权限码
     */
    private String roleCode;

    /**
     * 权限名称
     */
    private String roleName;
}
