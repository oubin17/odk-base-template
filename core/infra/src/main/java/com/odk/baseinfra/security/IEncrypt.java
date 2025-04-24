package com.odk.baseinfra.security;

/**
 * IEncrypt 加密类
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
public interface IEncrypt {


    /**
     * 自适应慢哈希算法（如 bcrypt/argon2）替代二次加密
     *
     * @param password
     * @return
     */
    String encrypt(String password);

    /**
     * 比对
     *
     * @param rowPassword
     * @param encodedPassword
     * @return
     */
    boolean matches(String rowPassword, String encodedPassword);

}
