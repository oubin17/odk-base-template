package com.odk.baseweb.nonbusiness;

import com.odk.baseapi.inter.nonbusiness.LlmApi;
import com.odk.baseutil.request.llm.LlmRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * LlmChatController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/3/4
 */
@Slf4j
@RestController
@RequestMapping("/llmchat")
public class LlmChatController {

    private final LlmApi llmApi;

    public LlmChatController(LlmApi llmApi) {
        this.llmApi = llmApi;
    }

    @PostMapping(value = "/msg", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter add(@RequestBody LlmRequest llmRequest) {
        return llmApi.chatMsg(llmRequest);
    }
}
