package com.odk.baseweb.interceptor.auth;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.odk.baseutil.enums.InnerRoleEnum;
import com.odk.baseweb.interceptor.CorsInterceptor;
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
        //trace id
        registry.addInterceptor(new TracerIdInterceptor());
        //注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(saInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 配置异步请求支持
        // 可以添加自定义的异步请求拦截器等配置
    }

    /**
     * 这里用于路由拦截鉴权
     * 如果遇到拦截无效，请检查头部是否带上cookie标识
     *
     * important：这里的路径，必须是从controller层开始的路径
     *
     * @return
     */
    private SaInterceptor saInterceptor() {
        String[] noLoginCheck = new String[]{"/user/query/loginId", "/user/register", "/user/login"};
        return new SaInterceptor(handler -> {
            // 指定一条 match 规则
            SaRouter.match("/**")    // 拦截的 path 列表，可以写多个 */
                    .notMatch(noLoginCheck)        // 排除掉的 path 列表，可以写多个
                    .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式

            // 根据路由划分模块，不同模块不同鉴权
            SaRouter.match("/permission/role/rela/**", r -> StpUtil.checkRole(InnerRoleEnum.ADMIN.getCode()));

//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
        });
    }
}
