package com.odk.baseutil.request.role;

import com.odk.base.vo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserRoleRelaRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleRelaRequest extends BaseRequest {
    @Serial
    private static final long serialVersionUID = 1395229907393357124L;

    /**
     * 角色id
     */
    @NotBlank(message = "角色 ID 不能为空")
    private String roleId;

    /**
     * 用户id
     */
    @NotBlank(message = "用户 ID 不能为空")
    private String userId;
}
