package com.odk.baseutil.validate;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * ValidationUtil - 通用验证工具类
 * 
 * 提供常用的格式验证方法：
 * - 手机号验证
 * - 邮箱验证
 * - 身份证验证
 * 等
 *
 * @author: oubin
 * @version: 1.0
 * @date: 2026/4/28
 */
public class ValidationUtil {

    /**
     * 中国大陆手机号正则表达式
     * 支持：13x, 14x, 15x, 16x, 17x, 18x, 19x 开头的11位号码
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 邮箱正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 身份证正则表达式（18位）
     */
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");

    /**
     * 私有构造函数，防止实例化
     */
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 手机号验证 ====================

    /**
     * 验证是否为有效的中国大陆手机号
     * 
     * @param mobile 手机号
     * @return 是否有效
     */
    public static boolean isValidMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 验证手机号，如果无效则抛出异常
     * 
     * @param mobile 手机号
     * @throws BizException 手机号无效时抛出
     */
    public static void validateMobile(String mobile) {
        if (!isValidMobile(mobile)) {
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, I18nUtil.getMessage("mobile.validation.error", mobile));
        }
    }

    /**
     * 验证手机号，如果无效则抛出指定错误码的异常
     * 
     * @param mobile 手机号
     * @param errorCode 错误码
     * @param errorMessage 错误消息
     * @throws BizException 手机号无效时抛出
     */
    public static void validateMobile(String mobile, BizErrorCode errorCode, String errorMessage) {
        if (!isValidMobile(mobile)) {
            throw new BizException(errorCode, errorMessage);
        }
    }

    // ==================== 邮箱验证 ====================

    /**
     * 验证是否为有效的邮箱地址
     * 
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证邮箱，如果无效则抛出异常
     * 
     * @param email 邮箱地址
     * @throws BizException 邮箱无效时抛出
     */
    public static void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "邮箱格式不正确");
        }
    }

    // ==================== 身份证验证 ====================

    /**
     * 验证是否为有效的18位身份证号
     * 
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        return ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 验证身份证号，如果无效则抛出异常
     * 
     * @param idCard 身份证号
     * @throws BizException 身份证号无效时抛出
     */
    public static void validateIdCard(String idCard) {
        if (!isValidIdCard(idCard)) {
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "身份证号格式不正确");
        }
    }

    // ==================== 综合验证 ====================

    /**
     * 验证字符串是否为手机号或用户ID
     * 
     * 用于验证码场景：
     * - 未登录场景（注册、登录）：verifyKey 应该是手机号
     * - 已登录场景（修改密码等）：verifyKey 可以是用户ID
     * 
     * @param verifyKey 待验证的key
     * @param requireMobile 是否必须是手机号
     * @return 验证结果
     */
    public static boolean isValidVerifyKey(String verifyKey, boolean requireMobile) {
        if (StringUtils.isBlank(verifyKey)) {
            return false;
        }
        
        if (requireMobile) {
            // 必须是手机号
            return isValidMobile(verifyKey);
        } else {
            // 可以是手机号或用户ID（这里简化处理，只要非空即可）
            // 如果需要更严格的验证，可以添加用户ID的格式校验
            return true;
        }
    }

    /**
     * 脱敏手机号：138****1234
     * 
     * @param mobile 手机号
     * @return 脱敏后的手机号
     */
    public static String maskMobile(String mobile) {
        if (!isValidMobile(mobile)) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 脱敏邮箱：t***@example.com
     * 
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }
}
