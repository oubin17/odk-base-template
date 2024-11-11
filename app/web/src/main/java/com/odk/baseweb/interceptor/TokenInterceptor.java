package com.odk.baseweb.interceptor;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.baseutil.constext.ServiceContextHolder;
import com.odk.baseutil.constext.TokenHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

/**
 * SessionInterceptor
 * 定义拦截器
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@Deprecated
public class TokenInterceptor implements AsyncHandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method method = ((HandlerMethod) handler).getMethod();
        //权限放开
        if (method.getAnnotation(NoLoginCondition.class) != null) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            logger.error("Authorization required: {}", request.getRequestURI());
            throw new BizException(BizErrorCode.TOKEN_MISSING);
        }
        if (isValidToken(token)) {
            //设置ThreadLocal等操作
        } else {
            throw new BizException(BizErrorCode.TOKEN_EXPIRED, "Authorization 已过期，请重新登录");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 在请求被处理之后执行的操作，相当于拦截请求的后置处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 在请求完成后执行的操作，包括在视图渲染之后执行（即在DispatcherServlet做了所有事情后）

        // 你可以在这里进行一些清理工作，例如释放资源等
    }

    /**
     * 检验token是否过期
     * 1.采用本地缓存；
     * 2.分布式缓存：Redis
     *
     * @param token
     * @return
     */
    private boolean isValidToken(String token) {
        String userId = TokenHolder.getUser(token);
        ServiceContextHolder.setUserId(userId);
        return StringUtils.isNotEmpty(userId);
    }
}
