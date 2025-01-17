package com.odk.baseutil.dto.user;

import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PasswordUpdateDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PasswordUpdateDTO extends DTO {

    /**
     * 密码类型
     */
    private String identifyType;

    /**
     * 旧密码
     */
    private String oldIdentifyValue;

    /**
     * 新密码
     */
    private String newIdentifyValue;

}
