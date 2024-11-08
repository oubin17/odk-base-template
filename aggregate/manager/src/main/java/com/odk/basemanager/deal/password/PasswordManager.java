package com.odk.basemanager.deal.password;

import com.odk.base.security.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * PsssowrdManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/7
 */
@Service
public class PasswordManager {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordManager(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 密码加密
     *
     * @param password
     * @return
     */
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
    public boolean matches(String rowPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rowPassword, encodedPassword);
    }
}
