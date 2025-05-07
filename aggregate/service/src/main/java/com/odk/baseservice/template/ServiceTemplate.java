package com.odk.baseservice.template;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.JacksonUtil;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.context.ServiceContextHolder;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * ServiceTemplate - 统一服务处理模板
 *
 * @description: 提供统一的业务流程处理模板，支持函数式编程风格。
 * @version: 1.0
 * @author: oubin on 2025/5/7
 */
@Slf4j
public class ServiceTemplate {

    @FunctionalInterface
    public interface BizHandler<REQ, RES> {
        RES process(REQ request) throws Throwable;
    }

    /**
     * 入口方法：执行通用服务处理逻辑（带结果转换）
     */
//    public static <REQ, RES> ServiceResponse<RES> execute(
//            BizScene scene,
//            REQ request,
//            Consumer<REQ> beforeProcess,
//            BizHandler<REQ, RES> handler,
//            Function<REQ, RES> resultConverter,
//            Consumer<ServiceResponse<RES>> afterProcess) {
//
//        return doExecute(scene, request, beforeProcess, handler, afterProcess);
//    }

    /**
     * 入口方法：执行通用服务处理逻辑（无结果转换）
     */
    public static <REQ, RES> ServiceResponse<RES> execute(
            BizScene scene,
            REQ request,
            Consumer<REQ> beforeProcess,
            BizHandler<REQ, RES> handler,
            Consumer<ServiceResponse<RES>> afterProcess) {

        return doExecute(scene, request, beforeProcess, handler, afterProcess);
    }

    /**
     * 实际执行方法（私有）
     */
    private static <REQ, RES> ServiceResponse<RES> doExecute(
            BizScene scene,
            REQ request,
            Consumer<REQ> beforeProcess,
            BizHandler<REQ, RES> handler,
//            Function<REQ, RES> resultConverter,
            Consumer<ServiceResponse<RES>> afterProcess) {

        long startTime = System.currentTimeMillis();
        logDigestLog(scene, request, "REQUEST");

        ServiceResponse<RES> response = null;

        try {
            // 参数校验
            Objects.requireNonNull(scene, "BizScene 不能为空");
            Objects.requireNonNull(handler, "BizHandler 不能为空");

            // 初始化上下文
            initContext(scene);

            //前置处理
            if (beforeProcess != null) {
                beforeProcess.accept(request);
            }

            // 核心处理
            RES result = handler.process(request);

            // 结果转换
//            RES data = resultConverter != null ? resultConverter.apply(request) : result;

            // 构造返回值
            response = ServiceResponse.valueOfSuccess(result);
            logDigestLog(scene, response, "RESPONSE");
        } catch (BizException e) {
            response = handleBizException(e);
        } catch (Error error) {
            if (error instanceof OutOfMemoryError) {
                throw error; // OOM 不建议吞掉
            }
            response = handleSystemException(error);
        } catch (Throwable t) {
            response = handleSystemException(t);
        } finally {
            // 后置处理
            if (afterProcess != null && response != null) {
                afterProcess.accept(response);
            }
            // 日志记录
            logSummary(scene, response, System.currentTimeMillis() - startTime);
            // 上下文清理
            clearContext();
        }
        return response;
    }

    private static void initContext(BizScene scene) {
        ServiceContextHolder.setSceneCode(scene);
    }

    private static void clearContext() {
        ServiceContextHolder.clear();
    }

    private static <RES> ServiceResponse<RES> handleBizException(BizException e) {
        return ServiceResponse.valueOfError(e.getErrorCode(), e.getMessage());
    }

    private static <RES> ServiceResponse<RES> handleSystemException(Throwable t) {
        return ServiceResponse.valueOfError(BizErrorCode.SYSTEM_ERROR, t.getMessage());
    }

    private static void logDigestLog(BizScene scene, Object obj, String type) {
        String loginId = SessionContext.getLoginIdOrDefault("-");
        String objStr = obj == null ? "-" : JacksonUtil.toJsonString(obj);
        String summary = String.format("[%s,%s,%s,%s]",
                scene.getCode(),
                loginId,
                type,
                objStr);
        log.info(summary);
    }

    private static void logSummary(BizScene scene, ServiceResponse<?> response, long duration) {
        String loginId = SessionContext.getLoginIdOrDefault("-");
        boolean success = response != null && response.isSuccess();
        String code = success || response == null ? "-" : response.getErrorCode();

        String summary = String.format("[%s,%s,%s,%s](%dms)",
                scene.getCode(),
                loginId,
                success ? "SUCCESS" : "FAIL",
                code,
                duration);
        log.info(summary);
    }
}
