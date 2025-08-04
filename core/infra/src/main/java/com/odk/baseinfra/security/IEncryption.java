package com.odk.baseinfra.security;

/**
 * IEncryption
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
public interface IEncryption {

    /**
     * 自适应慢哈希算法：加密
     *
     * @param rawData
     * @return
     */
    String bcryptEncode(String rawData);

    /**
     * 自适应慢哈希算法：匹配
     *
     * @param rawData
     * @param encodedData
     * @return
     */
    boolean bcryptMatches(String rawData, String encodedData);

    /**
     * rsa 公钥加密
     *
     * @param rawData
     * @return
     */
    String rsaEncode(String rawData);

    /**
     * rsa 私钥解密
     *
     * @param encodedData
     * @return
     */
    String rsaDecode(String encodedData);
}
