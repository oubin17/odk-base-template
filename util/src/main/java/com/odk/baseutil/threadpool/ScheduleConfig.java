package com.odk.baseutil.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * ScheduleConfig
 * 使用@Async支持异步调用，默认的线程池是单线程
 * 分布式调度需要支持 Quartz 等框架
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/13
 */
@Slf4j(topic = "topic-log")
@Configuration
public class ScheduleConfig {

//
//    @Async("asyncTaskExecutor")
//    @Scheduled(cron = "0/1 * * * * ?")
//    public void blockingTask2() throws InterruptedException {
//        Thread.sleep(1000); // 阻塞5秒
//        log.info("阻塞 1 秒，合起来 2秒blockingTask");
//
//    }


}
