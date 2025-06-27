package com.odk.baseutil.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VerificationCodeEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Data
public class VerificationCodeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1045015790678380286L;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码过期时间：秒
     */
    private int expireTime;

    /**
     * 单个验证码最大验证次数
     */
    private int verifyTimes;

    private int maxVerifyTimes;

    /**
     * 验证码生成时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime  createTime;

    /**
     * 验证码唯一 ID
     */
    private String uniqueId;

}
