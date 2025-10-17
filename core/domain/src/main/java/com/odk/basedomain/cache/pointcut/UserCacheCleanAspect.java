package com.odk.basedomain.cache.pointcut;

import com.google.common.collect.Maps;
import com.odk.basedomain.cache.CacheProcess;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * UserCacheCleanAspect
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Aspect
@Component
public class UserCacheCleanAspect implements ApplicationContextAware {

    private Map<String, CacheProcess> cacheProcessMap;

    // 拦截带有特定注解的方法
    @AfterReturning(pointcut = "@annotation(com.odk.basedomain.cache.pointcut.UserCacheClean)")
    public void cleanUserCache(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        UserCacheClean annotation = signature.getMethod().getAnnotation(UserCacheClean.class);

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String key) {
            UserCacheSceneEnum scene = annotation.scene();
            cacheProcessMap.get(scene.getCode()).evictCache(key);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cacheProcessMap = Maps.newHashMap();
        for (CacheProcess cacheProcess : applicationContext.getBeansOfType(CacheProcess.class).values()) {
            cacheProcessMap.put(cacheProcess.getCacheScene().getCode(), cacheProcess);
        }
    }
}
