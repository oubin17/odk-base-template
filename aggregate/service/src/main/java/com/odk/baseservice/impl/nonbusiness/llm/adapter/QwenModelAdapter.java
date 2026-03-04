package com.odk.baseservice.impl.nonbusiness.llm.adapter;

import com.odk.baseservice.impl.nonbusiness.llm.ModelEnum;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * QwenModelAdapter
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/11/28
 */
@Slf4j
@Service
public class QwenModelAdapter implements ModelAdapter {

    private String apiKey;

    private String baseUrl;

    private String modelName;

    @Override
    public ModelEnum getType() {
        return ModelEnum.QWEN3_32B_128K;
    }

    @Override
    public Flux<String> streamChat(String systemMessage, String userMessage, Map<String, Object> parameters) {
        return Flux.create(sink -> {
            try {
                OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                        .apiKey(apiKey)
                        .baseUrl(baseUrl)
                        .build();
                String noThink = "/no_think";
                if (parameters != null && parameters.containsKey("/no_think")) {
                    if (!(Boolean) parameters.get("/no_think")) {
                        //如果明确指明需要长思考
                        noThink = "";
                    }
                }
                // 构建优化的参数
                ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                        .model(modelName)
                        .addSystemMessage(systemMessage == null ? "你是一个有帮助的助手" : systemMessage)
                        .addUserMessage(userMessage + noThink)
                        .build();
                // 处理流式响应
                client.chat().completions().createStreaming(params)
                        .subscribe(chunk -> {
                            // 获取增量内容
                            String content = chunk.choices().get(0).delta().content().orElse("");

                            if (!content.isEmpty()) {
                                System.out.println("模型回复: " + content);
                                sink.next(content.stripLeading());
                            }
                            if (chunk.choices().get(0).finishReason().isPresent()) {
                                System.out.println("收到结束信号: " + chunk.choices().get(0).finishReason());
                                sink.complete();
                            }

                        });

            } catch (Exception e) {
                log.error("调用大模型发生异常: " + e.getMessage(), e);
                if (!sink.isCancelled()) {
                    sink.error(e);
                }
            }
        });
    }

    @Override
    public String chat(String systemMessage, String userMessage, Map<String, Object> parameters) {
        OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
        String noThink = "/no_think";
        if (parameters != null && parameters.containsKey("/no_think")) {
            if (!(Boolean) parameters.get("/no_think")) {
                //如果明确指明需要长思考
                noThink = "";
            }
        }
        // 构建优化的参数
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addSystemMessage(systemMessage == null ? "你是一个有帮助的助手" : systemMessage)
                .addUserMessage(userMessage + noThink)
                .build();


        try {
            // 直接获取并返回模型响应内容
            return client.chat().completions().create(params)
                    .thenApply(chatCompletion ->
                            chatCompletion.choices().get(0).message().content().orElse("").stripLeading()
                    )
                    .exceptionally(e -> {
                        log.error("调用大模型发生异常: " + e.getMessage(), e);
                        return "模型调用失败";
                    })
                    .join();
        } catch (Exception e) {
            log.error("调用大模型发生异常: " + e.getMessage(), e);
            throw new RuntimeException("模型调用失败", e);
        }


    }
}
