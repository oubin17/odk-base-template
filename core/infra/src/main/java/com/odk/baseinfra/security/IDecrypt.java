package com.odk.baseinfra.security;

/**
 * IDecrypt 解密类
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
public interface IDecrypt {

    /**
     * 私钥解密：一次加密
     *
     * @param encryptedBase64
     * @return
     */
    String decrypt(String encryptedBase64);

    /**
     * 公钥加密
     *
     * @param rawData
     * @return
     */
    String encrypt(String rawData);

}
