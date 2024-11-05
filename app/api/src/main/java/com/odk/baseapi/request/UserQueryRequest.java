package com.odk.baseapi.request;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserQueryRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -8631757065773576507L;

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;

}
