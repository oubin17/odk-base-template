package com.odk.baseutil.security;

import com.odk.base.security.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityConfig
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/7
 */
@Configuration
public class EncodeConfig {

    /**
     * 服务端加密存储
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
