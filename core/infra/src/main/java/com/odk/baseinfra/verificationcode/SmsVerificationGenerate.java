package com.odk.baseinfra.verificationcode;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.google.gson.Gson;
import com.odk.base.util.JacksonUtil;
import com.odk.baseinfra.verificationcode.entity.VerifyCodeEntity;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    /**
     * 产线需要调用运营商的接口，完成短信发送，本项目未实际接入，只模拟短信发送场景
     *
     * @return
     */
    @Override
    protected String generateVerificationCode(String phoneNumber) {

        // 生成6位数字验证码（包含前导零）
        String verificationCode = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));

        VerifyCodeEntity verifyCodeEntity = new VerifyCodeEntity();
        verifyCodeEntity.setCode(verificationCode);
        verifyCodeEntity.setMin("5");

        // 初始化凭证提供者
        StaticCredentialProvider credentialProvider = StaticCredentialProvider.create(
                Credential.builder()
                        .accessKeyId("")
                        .accessKeySecret("")
                        .build()
        );
        // Configure the Client
        try (AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(credentialProvider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build()) {

            // Parameter settings for API request
            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .phoneNumber(phoneNumber)
                    .signName("速通互联验证码")
                    .templateCode("100001")
                    .templateParam(JacksonUtil.toJsonString(verifyCodeEntity))
                    //验证码有效期5分钟
//                    .validTime(300L)
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            SendSmsVerifyCodeResponse resp = response.get();
            System.out.println(new Gson().toJson(resp));

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return verificationCode;

    }

}
