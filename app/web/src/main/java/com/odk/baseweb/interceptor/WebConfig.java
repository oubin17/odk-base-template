package com.odk.baseweb.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.odk.baseweb.interceptor.tracer.TracerIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 * 1.注册拦截器
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 MyInterceptor 拦截器，拦截所有路径
        registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");
        //注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
        //不配置token鉴权
//        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new TracerIdInterceptor());
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 配置异步请求支持
        // 可以添加自定义的异步请求拦截器等配置
    }
}
