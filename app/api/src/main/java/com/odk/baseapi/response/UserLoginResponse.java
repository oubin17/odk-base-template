package com.odk.baseapi.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserLoginResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginResponse extends UserQueryResponse {

    /**
     * 接口凭证
     */
    private String token;
}
