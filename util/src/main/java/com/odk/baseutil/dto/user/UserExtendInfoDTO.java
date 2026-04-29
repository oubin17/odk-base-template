package com.odk.baseutil.dto.user;

import com.odk.base.dto.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserExtendInfoDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserExtendInfoDTO extends DTO {

    /**
     * 隐私协议版本
     */
    @NotBlank(message = "隐私协议版本号不能为空")
    private String privacyVersion;
}
