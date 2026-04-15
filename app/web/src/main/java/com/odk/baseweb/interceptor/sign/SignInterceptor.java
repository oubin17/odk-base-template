package com.odk.baseweb.interceptor.sign;

import com.odk.base.exception.BizException;
import com.odk.redisspringbootstarter.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

import static com.odk.base.exception.BizErrorCode.SIGNING_ERROR;

/**
 * SignInterceptor
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/15
 */
@Component
public class SignInterceptor implements HandlerInterceptor {

    private static final String sign = "sign";
    private static final String timestamp = "timestamp";
    private static final String nonce = "nonce";

    private static final String needSignCheck = "signCheck";

    private static final String NONCE_PREFIX = "api:nonce:";


    @Value("${sa-token.token-name}")
    private String tokenName;

    @Value("${request.header.sign.check}")
    private boolean signCheck;

    @Value("${request.header.signsecret}")
    private String signSecret;

    @Autowired
    private RedisUtil redisUtil;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果配置的不需要检验签名，或者请求参数中含有signCheck，则不需要检验签名（方便调试）
        if (!signCheck || StringUtils.isNotBlank(request.getHeader(SignInterceptor.needSignCheck))) {
            return true;
        }

        String timestamp = request.getHeader(SignInterceptor.timestamp);
        String nonce = request.getHeader(SignInterceptor.nonce);
        String token = request.getHeader(tokenName);
        String sign = request.getHeader(SignInterceptor.sign);

        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce) || StringUtils.isBlank(token) || StringUtils.isBlank(sign)) {
            throw new BizException(SIGNING_ERROR, "缺少必要的签名参数");
        }
        long now = System.currentTimeMillis() / 1000;
        // 1. 校验时间戳（5分钟有效期）
        if (Math.abs(now - Long.parseLong(timestamp)) > 300) {
            throw new BizException(SIGNING_ERROR, "请求已过期，请重新发起请求");
        }

        // ====================== 🔥 核心：防重放 - nonce一次性校验 ======================
        String nonceKey = NONCE_PREFIX + nonce;
        // setIfAbsent = 不存在则设置，存在则返回false（代表重复使用）
        Boolean ifAbsent = redisUtil.setIfAbsent(nonceKey, "used", 300, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(ifAbsent)) {
            throw new BizException(SIGNING_ERROR, "请求已重复，请重新发起");
        }

        String signStr = "timestamp=" + timestamp + "&nonce=" + nonce + "&token=" + token + "&secret=" + signSecret;
        String md5Sign = DigestUtils.md5Hex(signStr).toUpperCase();
        if (!StringUtils.equals(sign, md5Sign)) {
            throw new BizException(SIGNING_ERROR, "签名验证失败");
        }
        return true;
    }

}
