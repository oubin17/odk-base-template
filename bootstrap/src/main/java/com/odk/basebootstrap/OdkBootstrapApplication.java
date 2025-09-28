package com.odk.basebootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.odk"})
//JPA扫描路径
@EnableJpaRepositories(basePackages = "com.odk.basedomain")
@EntityScan("com.odk.basedomain")
//开启审计功能，自动添加时间
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class OdkBootstrapApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(OdkBootstrapApplication.class, args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
