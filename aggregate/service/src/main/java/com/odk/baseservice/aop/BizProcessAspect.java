package com.odk.baseservice.aop;

import com.odk.base.util.JacksonUtil;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * BizProcessAspect - 业务处理切面
 * 拦截带有 @BizProcess 注解的方法，记录出入参和业务场景
 *
 * @author: oubin
 * @version: 1.0
 * @date: 2026/4/28
 */
@Slf4j
@Aspect
@Component
public class BizProcessAspect {

    /**
     * 环绕通知：拦截所有带有 @BizProcess 注解的方法
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(bizProcess)")
    public Object around(ProceedingJoinPoint joinPoint, BizProcess bizProcess) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取业务场景
        String bizScene = bizProcess.bizScene().getCode();
        String bizSceneDesc = bizProcess.bizScene().getDescription();
        
        // 获取方法名
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        
        // 记录入参
        Object[] args = joinPoint.getArgs();
        String argsStr = formatArgs(args);
        log.info("[BizProcess-Request] 业务场景: [{}]({}), 用户 ID:{}, 方法: {}, 入参: {}",
                bizScene, bizSceneDesc, SessionContext.getLoginIdOrDefault("-"), methodName, argsStr);
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            
            // 计算执行时间
            long executeTime = System.currentTimeMillis() - startTime;
            
            // 记录出参
            String resultStr = formatResult(result);
            log.info("[BizProcess-Response] 业务场景: [{}]({}), 用户 ID:{}, 方法: {}, 出参: {}, 耗时: {}ms",
                    bizScene, bizSceneDesc, SessionContext.getLoginIdOrDefault("-"), methodName, resultStr, executeTime);
            
            return result;
            
        } catch (Throwable throwable) {
            // 计算执行时间
            long executeTime = System.currentTimeMillis() - startTime;
            
            // 记录异常
            log.error("[BizProcess-Exception] 业务场景: [{}]({}), 用户 ID:{}, 方法: {}, 耗时: {}ms, 异常信息: {}",
                    bizScene, bizSceneDesc, SessionContext.getLoginIdOrDefault("-"), methodName, executeTime, throwable.getMessage());
            throw throwable;
        }
    }

    /**
     * 格式化参数
     *
     * @param args 参数数组
     * @return 格式化后的参数字符串
     */
    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(formatObject(args[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 格式化返回值
     *
     * @param result 返回值
     * @return 格式化后的返回值的字符串
     */
    private String formatResult(Object result) {
        return formatObject(result);
    }

    /**
     * 格式化对象（避免大对象序列化影响性能）
     *
     * @param obj 对象
     * @return 格式化后的字符串
     */
    private String formatObject(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        // 对于基本类型和简单对象，直接序列化
        if (isSimpleType(obj)) {
            return obj.toString();
        }
        
        // 对于复杂对象，尝试 JSON 序列化
        try {
            String json = JacksonUtil.toJsonString(obj);
            // 如果 JSON 太长，截取前 500 个字符
            if (json != null && json.length() > 500) {
                return json.substring(0, 500) + "...(truncated)";
            }
            return json;
        } catch (Exception e) {
            // 序列化失败时，返回类名和 toString
            return obj.getClass().getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
        }
    }

    /**
     * 判断是否为简单类型
     *
     * @param obj 对象
     * @return 是否为简单类型
     */
    private boolean isSimpleType(Object obj) {
        return obj instanceof String 
            || obj instanceof Number 
            || obj instanceof Boolean 
            || obj instanceof Character
            || obj instanceof Enum;
    }
}
