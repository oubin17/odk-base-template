package com.odk.baseinfra.security;

import com.odk.base.security.BCryptPasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BcryptEncrypt
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
@Service
@AllArgsConstructor
public class BcryptEncrypt implements IEncrypt {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encrypt(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rowPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rowPassword, encodedPassword);
    }
}
