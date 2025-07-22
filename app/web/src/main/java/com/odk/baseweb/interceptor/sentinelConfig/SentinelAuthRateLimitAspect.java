package com.odk.baseweb.interceptor.sentinelConfig;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.odk.baseutil.userinfo.SessionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * SentinelAuthRateLimitAspect
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/7/9
 */
@Aspect
@Component
public class SentinelAuthRateLimitAspect {

    /**
     * 切面 {@link SentinelResource}注解的方法
     *
     * @param pjp
     * @param sentinelResource
     * @return
     * @throws Throwable
     */
    @Around("@annotation(sentinelResource)")
    public Object around(ProceedingJoinPoint pjp, SentinelResource sentinelResource) throws Throwable {
        String userId = SessionContext.getLoginIdOrDefault("-");
        String resourceName = sentinelResource.value();
        Entry entry = null;
        try {
            // 手动埋点，并将 userId 作为参数传入
            entry = SphU.entry(resourceName, EntryType.IN, 1, userId);
            return pjp.proceed();
        } catch (BlockException ex) {
            // 限流或熔断异常处理
            return handleBlockException(pjp, ex);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    /**
     * 发生限流时，调用注解上的 blockHandler 方法
     *
     * @param pjp
     * @param ex
     * @return
     * @throws Throwable
     */
    private Object handleBlockException(ProceedingJoinPoint pjp, BlockException ex) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        SentinelResource annotation = method.getAnnotation(SentinelResource.class);

        if (annotation != null && !annotation.blockHandler().isEmpty()) {
            String blockHandlerName = annotation.blockHandler();
            Method blockHandlerMethod = pjp.getTarget().getClass().getMethod(blockHandlerName, BlockException.class);

            return blockHandlerMethod.invoke(pjp.getTarget(), ex);
        }

        // 默认返回限流提示（可选）
        return "【默认限流响应】请求过于频繁，请稍后再试";
    }
}
