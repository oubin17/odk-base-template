package com.odk.baseutil.dto.verificationcode;

import com.odk.base.dto.DTO;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VerificationCodeDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerificationCodeDTO extends DTO {



    /**
     * 验证码场景：登录、注册、找回密码等
     */
    private VerifySceneEnum verifyScene;

    /**
     * 未登录时
     * 1-手机号
     * 2-邮箱
     * 登录时
     * 3-用户 id
     *  {@link VerifyTypeEnum }
     */
    private String verifyType;

    /**
     * 登录 ID：手机号、邮箱、用户名、第三方登录唯一标识
     */
    private String verifyKey;


    //以下字段需要前端传递

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 验证码唯一 ID
     */
    private String uniqueId;

    /**
     * 填充验证信息
     *
     * @param verifyType
     * @param verifyKey
     * @param verifyScene
     */
    public void fillVerifyInfo(VerifySceneEnum verifyScene, VerifyTypeEnum verifyType, String verifyKey) {
        this.verifyScene = verifyScene;
        this.verifyType = verifyType.getCode();
        this.verifyKey = verifyKey;
    }
}
