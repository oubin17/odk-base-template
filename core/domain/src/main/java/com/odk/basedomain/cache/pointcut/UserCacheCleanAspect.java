package com.odk.basedomain.cache.pointcut;

import com.odk.baseutil.enums.UserCacheSceneEnum;
import com.odk.redisspringbootstarter.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserCacheCleanAspect
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Aspect
@Component
public class UserCacheCleanAspect {

    private RedisUtil redisUtil;

    // 拦截带有特定注解的方法
    @AfterReturning(pointcut = "@annotation(com.odk.basedomain.cache.pointcut.UserCacheClean)")
    public void cleanUserCache(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        UserCacheClean annotation = signature.getMethod().getAnnotation(UserCacheClean.class);

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String key) {
            UserCacheSceneEnum scene = annotation.scene();
            redisUtil.delete(UserCacheSceneEnum.generateCacheKey(scene, key));
        }
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
