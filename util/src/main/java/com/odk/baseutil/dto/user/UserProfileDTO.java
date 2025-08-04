package com.odk.baseutil.dto.user;

import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserProfileDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserProfileDTO extends DTO {

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户生日
     */
    private String birthDay;

}
