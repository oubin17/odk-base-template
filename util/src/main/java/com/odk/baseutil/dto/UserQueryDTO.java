package com.odk.baseutil.dto;

import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserQueryDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends DTO {

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;
}
