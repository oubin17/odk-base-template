package com.odk.baseutil.constext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * LocalCacheHolder
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/20
 */
public class TokenHolder {

    private static final String TOKEN = "default_token";

    private static final String USER_ID = "1";

    /**
     * key： token
     * value: userid
     */
    private static final Cache<String, String> tokenCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.DAYS) // 设置会话过期时间为30分钟
            .recordStats()
            .build();

    static {
        tokenCache.put(TOKEN, USER_ID);
    }

    // 创建会话并返回会话ID
    public static String createToken(String userId) {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, userId);
        return token;
    }

    // 根据会话ID获取用户信息
    public static String getUser(String token) {
        return tokenCache.getIfPresent(token);
    }

    // 移除会话
    public static void invalidateSession(String token) {
        tokenCache.invalidate(token);
    }

}
