package com.odk.baseapi.inter.nonbusiness;

import com.odk.baseutil.request.llm.LlmRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * LlmApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/3/4
 */
public interface LlmApi {

    /**
     * 大模型对话
     *
     * @param llmRequest
     * @return
     */
    SseEmitter chatMsg(LlmRequest llmRequest);
}
