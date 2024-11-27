package com.odk.baseapi.response;

import com.odk.baseapi.vo.PermissionVO;
import com.odk.baseapi.vo.UserRoleVo;
import lombok.Data;

import java.util.List;

/**
 * PermissionQueryResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Data
public class PermissionQueryResponse {

    private Long userId;

    private List<UserRoleVo> roles;

    private List<PermissionVO> permissions;
}
