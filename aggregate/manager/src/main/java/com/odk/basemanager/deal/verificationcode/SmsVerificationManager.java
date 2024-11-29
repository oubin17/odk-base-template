package com.odk.basemanager.deal.verificationcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SmsVerificationManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Slf4j
@Service
public class SmsVerificationManager {

    /**
     * 验证手机验证码是否匹配
     *
     * @param code
     * @return
     */
    public boolean verifySms(String code) {
        return true;
    }
}
