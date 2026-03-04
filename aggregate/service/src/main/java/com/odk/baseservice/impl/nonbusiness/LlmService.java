package com.odk.baseservice.impl.nonbusiness;

import com.odk.baseapi.inter.nonbusiness.LlmApi;
import com.odk.baseservice.impl.nonbusiness.llm.LlmModelFactory;
import com.odk.baseservice.impl.nonbusiness.llm.ModelEnum;
import com.odk.baseservice.impl.nonbusiness.llm.StreamCallback;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.request.llm.LlmRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * LlmService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/3/4
 */
@Slf4j
@Service
@AllArgsConstructor
public class LlmService extends AbstractApiImpl implements LlmApi {

    @Autowired
    private LlmModelFactory llmModelFactory;

    // 创建专用线程池
    private static final ThreadPoolExecutor aiAnalysisExecutor = new ThreadPoolExecutor(
            3,  // 核心线程数
            10, // 最大线程数
            60L, // 空闲线程存活时间
            TimeUnit.SECONDS, // 时间单位
            new LinkedBlockingQueue<>(100), // 工作队列
            r -> new Thread(r, "ai-analysis-thread-" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
    );


    @Override
    public SseEmitter chatMsg(LlmRequest llmRequest) {

        SseEmitter emitter = new SseEmitter(2 * 60 * 1000L);
        // 设置连接超时和错误处理
        emitter.onTimeout(() -> {
            log.warn("SSE连接超时，taskId: {}", llmRequest.getInputMsg());
            emitter.complete();
        });
        emitter.onError((ex) -> {
            log.error("SSE连接错误，taskId: {}", llmRequest.getInputMsg(), ex);
            emitter.complete();
        });
        // 实现回调处理器
        StreamCallback callback = new StreamCallback() {
            @Override
            public void onData(String data) {
                try {
                    emitter.send(SseEmitter.event().data(data));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onComplete() {
                emitter.complete();
            }

            @Override
            public void onError(String error) {
                emitter.completeWithError(new RuntimeException(error));
            }
        };
        // 异步执行 AI 分析
        clientAiAnalysis(llmRequest.getInputMsg(), callback);
        return emitter;
    }

    public void clientAiAnalysis(String inputMsg, StreamCallback callback) {
        // 使用线程池执行 AI 分析任务
        log.info("开始AI分析日志打印，inputMsg={}", inputMsg);

        aiAnalysisExecutor.execute(() -> extracted(inputMsg, callback));
    }

    private void extracted(String inputMsg, StreamCallback callback) {
        try {

            String prompt = "You are a helpful assistant. Please answer the question based on the given information. If you do not know the answer, please say you do not know. Do not make up an answer.\n" + inputMsg;
            llmModelFactory.getAdapter(ModelEnum.QWEN3_32B_128K).streamChat(null, prompt, null)
                    .subscribe(
                            callback::onData,
                            error -> {
                                log.error("AI分析过程发生错误，inputMsg={},error={}", inputMsg, error.getMessage());
                                callback.onError(error.getMessage());
                            },
                            () -> {
                                log.info("AI分析完成，inputMsg={}", inputMsg);
                                callback.onComplete();
                            }
                    );

        } catch (Exception e) {
            log.error("AI分析过程发生异常，inputMsg={},error=", inputMsg, e);
            callback.onError(e.getMessage());
        }
    }

}
