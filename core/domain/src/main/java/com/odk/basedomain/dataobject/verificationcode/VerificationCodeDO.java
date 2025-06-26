package com.odk.basedomain.dataobject.verificationcode;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * VerificationCodeDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_verification_code", indexes = {
        @Index(name = "idx_verify_id", columnList = "verify_key,verify_type"),
        @Index(name = "idx_unique_id", columnList = "unique_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class VerificationCodeDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 3181194678028195247L;

    /**
     * 手机号、邮箱
     */
    @Column(name = "verify_key")
    private String verifyKey;

    /**
     * 验证码类型：手机、邮箱、用户名、第三方登录唯一标识
     */
    @Column(name = "verify_type")
    private String verifyType;

    /**
     * 验证码场景：登录、注册、找回密码等
     */
    @Column(name = "verify_scene")
    private String verifyScene;

    /**
     * 验证次数
     */
    @Column(name = "verify_times")
    private int verifyTimes;

    /**
     * 验证结果：0-未验证，1-验证成功，2-验证失败
     */
    @Column(name = "result")
    private int result;

    /**
     * 唯一标识
     */
    @Column(name = "unique_id")
    private String uniqueId;

    /**
     * 用户id 可能为空，
     */
    @Column(name = "user_id")
    private String userId;
}
