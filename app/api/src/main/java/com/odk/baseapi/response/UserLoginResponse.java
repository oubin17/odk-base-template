package com.odk.baseapi.response;

import com.odk.baseutil.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserLoginResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginResponse extends UserEntity {

    @Serial
    private static final long serialVersionUID = -762087251627529872L;

    /**
     * 接口凭证
     */
    private String token;
}
