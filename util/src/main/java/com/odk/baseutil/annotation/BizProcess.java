package com.odk.baseutil.annotation;

import com.odk.baseutil.enums.BizScene;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BizProcess
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizProcess {

    /**
     * 业务场景
     *
     * @return
     */
    BizScene bizScene();
}
