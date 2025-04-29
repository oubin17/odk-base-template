package com.odk.baseinfra.verificationcode;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * SmsVerificationGenerate
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Service
public class SmsVerificationGenerate extends AbstractVerificationGenerate {




    @Override
    protected String generateVerificationCode() {
        // 生成6位数字验证码（包含前导零）
        return String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));
    }

    /**
     * 这里需要调用运营商的接口，完成短信发送，本项目未实际接入，只模拟短信发送场景
     *
     * @param tokenTypeEnum
     * @return
     */
//    @Override
//    public String generateVerificationCode(String tokenId, TokenTypeEnum tokenTypeEnum, VerifySceneEnum sceneEnum) {
//        // 生成6位数字验证码（包含前导零）
//        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));

//        return null;
//    }
}
