package com.odk.basedomain.cache.pointcut;

import com.odk.base.enums.cache.CacheActionEnum;
import com.odk.baseutil.enums.UserCacheSceneEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UserCacheClean
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCacheClean {
    UserCacheSceneEnum scene();
    CacheActionEnum action() default CacheActionEnum.DELETE;
}
