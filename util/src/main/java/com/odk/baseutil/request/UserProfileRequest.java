package com.odk.baseutil.request;

import com.odk.base.enums.user.UserGenderEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserProfileRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserProfileRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 9203079406377275661L;

    /**
     * 用户姓名
     */
    @NotBlank(message = "userName不能为空")
    @Size(min = 1, max = 10, message = "userName长度必须在1-10之间")
    private String userName;

    /**
     * 用户性别
     */
    @NotBlank(message = "gender不能为空")
    @EnumValue(enumClass = UserGenderEnum.class, property = "code", message = "gender非法")
    private String gender;

    /**
     * 用户生日
     */
    @NotBlank(message = "birthDay不能为空")
    @Size(min = 1, max = 20, message = "birthDay长度必须在1-20之间")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "birthDay格式不正确")
    private String birthDay;
}
