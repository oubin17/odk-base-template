package com.odk.baseutil.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * EnumValue
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/4
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    /**
     * 枚举类
     * @return
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 要校验的枚举属性字段名（默认使用枚举名称）
     */
    String property() default "name";

    /**
     * 错误提示信息
     *
     * @return
     */
    String message() default "必须为指定枚举值";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 是否忽略大小写
     *
     * @return
     */
    boolean ignoreCase() default false;
}
