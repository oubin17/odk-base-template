package com.odk.baseutil.monitor;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

/**
 * SystemMonitor
 * 系统监控类，使用 @Scheduled 定时任务输出监控数据，更完善的监控方式需要使用普米等监控工具统一收集监控指标，grafana 生成可视化视图
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/25
 */
@Slf4j(topic = "topic-log")
@Configuration
public class SystemMonitor {


    private DataSource dataSource;

    private WebServerApplicationContext webServerApplicationContext;

    /**
     * 数据库连接池监控，每分钟的第 1 秒执行
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "1 * * * * ?")
    public void dataSourceMonitor() {
        DruidDataSource druidDataSource =  (DruidDataSource) dataSource;
        log.info("数据库连接池监控,最大连接数-{},活跃连接数-{},空闲连接数-{},等待连接线程数-{}", druidDataSource.getMaxActive(), druidDataSource.getActiveCount(), druidDataSource.getPoolingCount(), druidDataSource.getWaitThreadCount());

    }

    /**
     * Tomcat连接池监控，每分钟的第 11 秒执行
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "11 * * * * ?")
    public void tomcatMonitor() {

        Tomcat tomcat = ((TomcatWebServer) webServerApplicationContext.getWebServer()).getTomcat();
        if (tomcat == null || tomcat.getService().findConnectors().length == 0) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) tomcat
                .getConnector()
                .getProtocolHandler()
                .getExecutor();
        log.info("Tomcat连接池监控,最大线程数-{},活跃线程数-{},阻塞队列长度-{}", threadPoolExecutor.getMaximumPoolSize(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getQueue().size());
    }
//
//    @Async("asyncTaskExecutor")
//    @Scheduled(cron = "0/1 * * * * ?")
//    public void blockingTask2() throws InterruptedException {
//        Thread.sleep(1000); // 阻塞5秒
//        log.info("阻塞 1 秒，合起来 2秒blockingTask");
//
//    }


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setWebServerApplicationContext(WebServerApplicationContext webServerApplicationContext) {
        this.webServerApplicationContext = webServerApplicationContext;
    }
}
