package com.odk.baseservice.impl.nonbusiness.llm.adapter;

import com.odk.baseservice.impl.nonbusiness.llm.ModelEnum;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * ModelAdapter
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/11/28
 */
public interface ModelAdapter {

    /**
     * 获取适配器类型
     */
    ModelEnum getType();

    /**
     * 流式输出 调用模型
     * @param systemMessage 系统角色设定，可以为空
     * @param userMessage 用户提示词
     * @param parameters 模型参数，可以为空，默认禁用长思考:/no_think,
     * @return
     */
    Flux<String> streamChat(String systemMessage, String userMessage, Map<String, Object> parameters);

    /**
     * 模型调用
     *
     * @param systemMessage
     * @param userMessage
     * @param parameters
     * @return
     */
    String chat(String systemMessage, String userMessage, Map<String, Object> parameters);
}
