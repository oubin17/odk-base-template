package com.odk.baseinfra.verificationcode;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * LocalVerificationGenerate
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/26
 */
@Service
public class LocalVerificationGenerate extends AbstractVerificationGenerate {
    @Override
    protected String generateVerificationCode(String phoneNumber) {
        // 生成6位数字验证码（包含前导零）
        return String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));
    }
}
