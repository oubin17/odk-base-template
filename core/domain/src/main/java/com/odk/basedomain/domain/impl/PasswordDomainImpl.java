package com.odk.basedomain.domain.impl;

import com.odk.base.security.BCryptPasswordEncoder;
import com.odk.basedomain.domain.PasswordDomain;
import org.springframework.stereotype.Service;

/**
 * PasswordDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Service
public class PasswordDomainImpl implements PasswordDomain {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordDomainImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    @Override
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * 密码是否匹配
     *
     * @param rowPassword
     * @param encodedPassword
     * @return
     */
    @Override
    public boolean matches(String rowPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rowPassword, encodedPassword);
    }
}
