package com.odk.baseutil.request;


import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * HelloWorldRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2023/11/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelloWorldRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 5269777314564109356L;

    /**
     * 名称
     */
    private String name;

}
