package com.odk.baseutil.request.role;

import com.odk.base.vo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "角色码不能为空")
    private String roleCode;

    /**
     * 权限名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
}
