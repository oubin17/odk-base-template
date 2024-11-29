package com.odk.basedomain.domain;

/**
 * PasswordDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
public interface PasswordDomain {

    /**
     * 加密
     *
     * @param password
     * @return
     */
    String encode(String password);

    /**
     * 比对
     *
     * @param rowPassword
     * @param encodedPassword
     * @return
     */
    boolean matches(String rowPassword, String encodedPassword);
}
