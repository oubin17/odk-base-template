package com.odk.baseweb.interceptor;

import java.lang.annotation.*;

/**
 * NoLoginRequried
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLoginCondition {
}
