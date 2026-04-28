package com.odk.basemanager.api.verificationcode;

import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;

import java.util.function.Consumer;

/**
 * IVerificationCodeManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IVerificationCodeManager {

    VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO);

    boolean compare(VerificationCodeDTO verificationCodeDTO);

    boolean compareAndIncr(VerificationCodeDTO verificationCodeDTO);

    /**
     * 验证码校验成功后执行回调操作并返回结果（事务保证）
     * 
     * @param verificationCodeDTO 验证码信息
     * @param consumer 验证成功后的回调操作，返回结果，如果回调抛出异常，整个操作会回滚
     * @param <T> 返回类型
     * @return 回调操作的返回结果，如果验证失败则返回 null
     */
    default <T> boolean verifyAndExecute(VerificationCodeDTO verificationCodeDTO, Consumer<T> consumer) {
        boolean compare = compare(verificationCodeDTO);
        if (compare) {
            consumer.accept(null);
        }
        return compareAndIncr(verificationCodeDTO);
    }
}
