package com.odk.baseutil.request.role;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * PermissionAddRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionAddRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -7761396479818502328L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限名称
     */
    private String permissionName;

}
