package com.odk.baseutil.request.llm;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * LlmRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/3/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LlmRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -189160396928070974L;

    private String inputMsg;
}
