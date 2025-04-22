package com.odk.baseutil.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AsyncConfig
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/22
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    public static final Executor COMMON_EXECUTOR = new ThreadPoolExecutor(
            2, 10, 600, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder()
                    .setNameFormat("common-pool-%d") // %d表示线程编号
                    .build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 在需要异步的方法上，添加注解    @Async("asyncTaskExecutor")
     *
     * 自定义异步任务线程池
     * 核心参数说明：
     * - corePoolSize: 常驻线程数（建议根据 CPU 核数设置）
     * - maxPoolSize: 最大线程数（建议为核心数的 2 倍）
     * - queueCapacity: 任务队列容量（根据业务吞吐量设置）
     * - keepAliveSeconds: 空闲线程存活时间（秒）
     * - threadNamePrefix: 线程名前缀（便于日志追踪）
     */
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // CPU 密集型建议：核心数+1 | IO 密集型建议：核心数*2
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 1);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async-spring-boot-");

        // 设置拒绝策略（重要！）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
