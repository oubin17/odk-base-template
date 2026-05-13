package com.odk.baseweb.nonbusiness;

import cn.dev33.satoken.annotation.SaIgnore;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.I18nUtil;
import com.odk.base.vo.response.ServiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * I18nTestController - 国际化测试控制器
 *
 * @description: 用于测试国际化功能
 * @version: 1.0
 * @author: oubin on 2026/4/23
 */
@RestController
@RequestMapping("/system/i18n")
public class I18nTestController {

    /**
     * 测试国际化消息获取
     * 访问示例:
     * - 中文: GET /system/i18n/test (默认)
     * - 英文: GET /system/i18n/test?lang=en_US
     * - 或在请求头设置: Accept-Language: en-US
     *
     * @return 国际化消息
     */
    @SaIgnore
    @GetMapping("/test")
    public ServiceResponse<String> testI18n() {
        // 测试不同的消息码
        String signMessage = I18nUtil.getMessage("user.default.name");
        String systemMessage = I18nUtil.getMessage("verification.data.isnull");
        String paramMessage = I18nUtil.getMessage("user.password.empty");
        
        // 测试带参数的消息
        String modelMessage = I18nUtil.getMessage("model.unsupported", "GPT-4");
        String enumMessage = I18nUtil.getMessage("enum.property.not.found", "code", "UserStatusEnum");
        
        StringBuilder result = new StringBuilder();
        result.append("Current Language Messages:\n");
        result.append("1. Sign verification: ").append(signMessage).append("\n");
        result.append("2. System error: ").append(systemMessage).append("\n");
        result.append("3. Password empty: ").append(paramMessage).append("\n");
        result.append("4. Model unsupported: ").append(modelMessage).append("\n");
        result.append("5. Enum property not found: ").append(enumMessage);
        
        return ServiceResponse.valueOfSuccess(result.toString());
    }

    /**
     * 测试国际化异常消息
     * 访问示例:
     * - 中文: GET /system/i18n/exception (默认)
     * - 英文: GET /system/i18n/exception?lang=en_US
     *
     * @return 异常响应（会被GlobalExceptionHandler拦截并返回国际化消息）
     */
    @SaIgnore
    @GetMapping("/exception")
    public ServiceResponse<Void> testException() {
        // 抛出业务异常，测试GlobalExceptionHandler的国际化处理
        throw new BizException(BizErrorCode.VERIFY_CODE_COMPARE_MAX_TIMES);
    }
}
