package com.odk.baseweb.verificationcode;

import cn.dev33.satoken.annotation.SaIgnore;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.verificationcode.VerificationCodeApi;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.request.VerificationCodeRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VerificationCodeController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@RestController
@RequestMapping("/verifycode")
public class VerificationCodeController {

    private VerificationCodeApi verificationCodeApi;

    /**
     * 生成验证码
     *
     * @param codeRequest
     * @return
     */
    @SaIgnore
    @PostMapping("/generate")
    ServiceResponse<VerificationCodeEntity> generate(@RequestBody @Valid VerificationCodeRequest codeRequest) {
        return this.verificationCodeApi.generateCode(codeRequest);
    }

    /**
     * 校验验证码
     *
     * 产线不应该暴露该接口，只是为了测试方便。
     *
     * @param codeRequest
     * @return
     */
    @SaIgnore
    @PostMapping("/compare")
    ServiceResponse<Boolean> compare(@RequestBody @Valid VerificationCodeRequest codeRequest) {
        return this.verificationCodeApi.compare(codeRequest);
    }

    @Autowired
    public void setVerificationCodeApi(VerificationCodeApi verificationCodeApi) {
        this.verificationCodeApi = verificationCodeApi;
    }
}
