package com.odk.baseutil.validate;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * EnumValueValidator：通过反射获取字段，对性能有极致要求的勿用，直接在代码中校验
 *
 * <p>
 * 默认使用枚举名称校验，可以自定义校验的属性，如code 等等。
 * 优先通过 get 方法获取，如果无 get 方法，再获取对应字段。
 * </p>
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/4
 */
@Slf4j
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    private static final ConcurrentMap<CacheKey, Set<Object>> VALUE_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Map<String, Accessor>> ACCESSOR_CACHE = new ConcurrentHashMap<>();

    private EnumValue annotation;
    private Set<Object> allowedValues;
    private boolean isNameProperty;
    private Class<?> targetType;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        String property = annotation.property();

        // 检查是否为枚举名称校验
        this.isNameProperty = "name".equalsIgnoreCase(property);

        // 从缓存获取或生成允许值集合
        CacheKey cacheKey = new CacheKey(enumClass, property, annotation.ignoreCase());
        this.allowedValues = VALUE_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                return precomputeAllowedValues(enumClass, property);
            } catch (Exception e) {
                log.error("初始化枚举校验器失败", e);
                throw new BizException(BizErrorCode.SYSTEM_ERROR, "枚举校验初始化失败");
            }
        });

        // 确定目标类型
        if (!allowedValues.isEmpty()) {
            this.targetType = allowedValues.iterator().next().getClass();
        }
    }

    private Set<Object> precomputeAllowedValues(Class<? extends Enum<?>> enumClass, String property)
            throws Exception {
        Set<Object> values = new HashSet<>();
        Enum<?>[] constants = enumClass.getEnumConstants();

        if (isNameProperty) {
            // 直接使用枚举名称
            for (Enum<?> constant : constants) {
                values.add(annotation.ignoreCase() ?
                        constant.name().toLowerCase() :
                        constant.name());
            }
        } else {
            // 获取属性访问器
            Map<String, Accessor> accessors = ACCESSOR_CACHE.computeIfAbsent(enumClass, clazz -> {
                try {
                    return buildAccessors(clazz);
                } catch (Exception e) {
                    log.error("构建访问器失败", e);
                    throw new BizException(BizErrorCode.SYSTEM_ERROR, "构建访问器失败");
                }
            });

            Accessor accessor = accessors.get(property.toLowerCase());
            if (accessor == null) {
                log.error("枚举类 {} 不存在属性: {}", enumClass.getName(), property);
                throw new BizException(BizErrorCode.SYSTEM_ERROR, "枚举类 " + enumClass.getName() +
                        " 不存在属性: " + property);
            }

            // 获取所有枚举值
            for (Enum<?> constant : constants) {
                Object value = accessor.getValue(constant);
                if (annotation.ignoreCase() && value instanceof String) {
                    value = ((String) value).toLowerCase();
                }
                values.add(value);
            }
        }
        return Collections.unmodifiableSet(values);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        Object valueToCheck = preprocessValue(value);
        return allowedValues.contains(valueToCheck);
    }

    private Object preprocessValue(Object value) {
        // 自动类型转换
        if (value instanceof String && !targetType.equals(String.class)) {
            try {
                if (targetType == Integer.class) {
                    return Integer.parseInt((String) value);
                } else if (targetType == Long.class) {
                    return Long.parseLong((String) value);
                }
            } catch (NumberFormatException ignored) {
                // 转换失败保持原值
            }
        }

        // 统一大小写处理
        if (annotation.ignoreCase() && value instanceof String) {
            return ((String) value).toLowerCase();
        }
        return value;
    }

    // 缓存键定义
    private record CacheKey(Class<?> enumClass, String property, boolean ignoreCase) {
    }

    // 访问器抽象
    private interface Accessor {
        Object getValue(Enum<?> enumConstant);
    }

    // 构建属性访问器
    private Map<String, Accessor> buildAccessors(Class<?> enumClass) throws Exception {
        Map<String, Accessor> accessors = new HashMap<>();

        // 扫描所有公共方法
        for (Method method : enumClass.getMethods()) {
            if (method.getParameterCount() == 0 &&
                    method.getName().startsWith("get")) {
                String property = uncapitalize(method.getName().substring(3));
                accessors.put(property.toLowerCase(), new MethodAccessor(method));
            }
        }

        // 扫描所有字段
        for (Field field : enumClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                accessors.put(field.getName().toLowerCase(), new FieldAccessor(field));
            }
        }

        return accessors;
    }

    // 方法访问器实现
    private record MethodAccessor(Method method) implements Accessor {
        private MethodAccessor(Method method) {
            this.method = method;
            method.setAccessible(true);
        }

        @Override
        public Object getValue(Enum<?> enumConstant) {
            try {
                return method.invoke(enumConstant);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 字段访问器实现
    private record FieldAccessor(Field field) implements Accessor {
        private FieldAccessor(Field field) {
            this.field = field;
            field.setAccessible(true);
        }

        @Override
        public Object getValue(Enum<?> enumConstant) {
            try {
                return field.get(enumConstant);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
