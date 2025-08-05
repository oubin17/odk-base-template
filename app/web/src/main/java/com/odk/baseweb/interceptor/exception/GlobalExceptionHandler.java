package com.odk.baseweb.interceptor.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.vo.response.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

/**
 * GlobalExceptionHandler
 * 全局异常处理器，@ExceptionHandler注解来指定处理CustomValidationException异常的方法。
 * 在这个方法中，你可以定义如何处理校验失败的情况，例如返回特定的HTTP状态码和错误信息。
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ServiceResponse<Void>> handleBizException(BizException e) {
        log.error("业务异常", e);
        // 处理校验异常，可以根据需要返回适当的响应
        return new ResponseEntity<>(ServiceResponse.valueOfError(e.getErrorCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理SaToken异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<ServiceResponse<Void>> handleSaTokenException(SaTokenException e) {
        log.error("Token 校验异常", e);
        // 处理校验异常，可以根据需要返回适当的响应
        if (e instanceof NotLoginException) {
            return new ResponseEntity<>(ServiceResponse.valueOfError(BizErrorCode.TOKEN_UNMATCHED, "token无效"), HttpStatus.BAD_REQUEST);
        } else if (e instanceof NotRoleException) {
            return new ResponseEntity<>(ServiceResponse.valueOfError(BizErrorCode.PERMISSION_DENY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ServiceResponse.valueOfError(BizErrorCode.SYSTEM_ERROR, "系统异常"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理系统未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse<Void>> handleUnknownException(Exception e) {
        log.error("未知系统异常", e);
        return new ResponseEntity<>(ServiceResponse.valueOfError(BizErrorCode.SYSTEM_ERROR, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServiceResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
        log.error("参数校验异常", ex);
        Optional<String> firstError = ex.getBindingResult().getAllErrors().stream().findFirst().map(ObjectError::getDefaultMessage);

        return new ResponseEntity<>(ServiceResponse.valueOfError(BizErrorCode.PARAM_ILLEGAL, firstError.orElse("系统异常")), HttpStatus.BAD_REQUEST);
    }



}
