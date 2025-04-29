package com.odk.baseservice.impl.verificationcode;

import com.odk.base.util.JacksonUtil;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.verificationcode.VerificationCodeApi;
import com.odk.basemanager.deal.verificationcode.VerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.context.ServiceContextHolder;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.mapper.VerificationCodeMapper;
import com.odk.baseutil.request.VerificationCodeRequest;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * VerificationCodeService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Slf4j
@Service
public class VerificationCodeService extends AbstractApiImpl implements VerificationCodeApi {

    private VerificationCodeManager verificationCodeManager;

    private VerificationCodeMapper verificationCodeMapper;

    private RedisUtil redisUtil;

    @Override
    public ServiceResponse<VerificationCodeEntity> generateCode(VerificationCodeRequest codeRequest) {
        return super.strictBizProcess(BizScene.VERIFICATION_CODE_GENERATE, codeRequest, new StrictApiCallBack<VerificationCodeEntity, VerificationCodeEntity>() {

            @Override
            protected Object convert(BaseRequest request) {
                return verificationCodeMapper.toDTO(codeRequest);
            }

            @Override
            protected VerificationCodeEntity doProcess(Object args) {
                VerificationCodeDTO verificationCodeDTO = (VerificationCodeDTO) args;
                return verificationCodeManager.generateCode(verificationCodeDTO);
            }

            @Override
            protected VerificationCodeEntity convertResult(VerificationCodeEntity apiResult) {
                return apiResult;
            }
        });
    }

    @Override
    public ServiceResponse<Boolean> compare(VerificationCodeRequest codeRequest) {
        return super.strictBizProcess(BizScene.VERIFICATION_CODE_COMPARE, codeRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected Object convert(BaseRequest request) {
                return verificationCodeMapper.toDTO(codeRequest);
            }

            @Override
            protected Boolean doProcess(Object args) {
                VerificationCodeDTO verificationCodeDTO = (VerificationCodeDTO) args;
                return verificationCodeManager.checkVerificationCode(verificationCodeDTO);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

            @Override
            protected ServiceResponse<Boolean> assembleResult(Boolean apiResult) throws Throwable {
                ServiceResponse<Boolean> response = super.assembleResult(apiResult);
                if (!apiResult) {
                    //如果校验失败
                    String jsonString = JacksonUtil.toJsonString(ServiceContextHolder.getServiceContext());
                    response.setExtendInfo(JacksonUtil.parseObject(jsonString, Map.class));
                }
                return response;
            }

        });
    }

    @Autowired
    public void setVerificationCodeManager(VerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }

    @Autowired
    public void setVerificationCodeMapper(VerificationCodeMapper verificationCodeMapper) {
        this.verificationCodeMapper = verificationCodeMapper;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
