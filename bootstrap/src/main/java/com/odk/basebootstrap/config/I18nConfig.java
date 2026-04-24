package com.odk.basebootstrap.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.Locale;

/**
 * I18nConfig - 国际化配置
 *
 * @description: 配置Spring国际化支持，包括消息源、区域解析器和拦截器
 * @version: 1.0
 * @author: oubin on 2026/4/23
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * 配置消息源
     * 从classpath下的messages文件读取国际化消息
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 设置消息文件位置（classpath根目录）
        messageSource.setBasename("classpath:i18n/messages");
        // 设置默认编码为UTF-8
        messageSource.setDefaultEncoding("UTF-8");
        // 设置缓存时间（秒），-1表示永不过期，开发环境可设置为0便于调试
        messageSource.setCacheSeconds(-1);
        // 如果找不到对应语言的message，使用默认语言
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
    }

    /**
     * 配置区域解析器
     * 基于请求头Accept-Language自动识别用户语言偏好
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        // 设置支持的语言列表
        resolver.setSupportedLocales(Arrays.asList(
                Locale.SIMPLIFIED_CHINESE,
                Locale.US,
                Locale.ENGLISH
        ));
        // 设置默认语言为中文
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }

    /**
     * 配置区域变更拦截器
     * 允许通过URL参数lang动态切换语言，例如: ?lang=en_US
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // 设置语言参数名，默认为locale
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
