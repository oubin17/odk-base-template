package com.odk.baseutil.request.role;

import com.odk.base.vo.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * PersmissionQueryRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 8724274869827098577L;

    /**
     * 角色id
     */
    private String roleId;


}
