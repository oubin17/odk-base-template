package com.odk.baseinfra.security;

/**
 * IDecrypt 解密类
 * 整体的加解密流程是：前端先通过公钥加密，将密文传给后端，后端调用 {@link IDecrypt#decrypt(String)} 接口进行解密
 * 解密成功后，后端再通过 {@link IEncrypt#encrypt(String)} 接口进行二次加密，保存到数据库中
 * 特殊情况下，在公钥加密前，可以再通过 hash 加密，防止服务端拿到明文密码，但是这样会降低密码的熵值。
 * 公私钥生产环境不要存储在本地，可以使用第三方服务，如阿里云，AWS 等。
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
