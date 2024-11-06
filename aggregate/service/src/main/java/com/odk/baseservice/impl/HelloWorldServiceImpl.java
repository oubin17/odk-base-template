package com.odk.baseservice.impl;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.HelloWorldApi;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.basemanager.dto.HelloWorldDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.HelloWorldRequest;
import com.odk.baseutil.response.HelloWorldResponse;
import org.springframework.stereotype.Service;

/**
 * HelloWorldServiceImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@Service
public class HelloWorldServiceImpl extends AbstractApiImpl implements HelloWorldApi {

    @Override
    public ServiceResponse<HelloWorldResponse> helloWorld(HelloWorldRequest helloWorldRequest) {
        return super.bizProcess(BizScene.HELLO_WORLD, helloWorldRequest, HelloWorldResponse.class, new ApiCallBack<String, HelloWorldResponse>() {
            @Override
            protected void checkParams(BaseRequest request) {
                super.checkParams(request);
                HelloWorldRequest hello = (HelloWorldRequest) request;
                AssertUtil.notNull(hello.getName(), BizErrorCode.PARAM_ILLEGAL, "request is null.");
            }

            @Override
            protected Object convert(BaseRequest request) {
                HelloWorldRequest hello = (HelloWorldRequest) request;
                HelloWorldDTO dto = new HelloWorldDTO();
                dto.setName(hello.getName());
                return dto;
            }

            @Override
            protected String doProcess(Object args) {
                HelloWorldDTO dto = (HelloWorldDTO) args;
                return "hello world";
            }

            @Override
            protected HelloWorldResponse convertResult(String apiResult) {
                HelloWorldResponse response = new HelloWorldResponse();
                response.setResult(apiResult);
                return response;
            }
        });
    }

}
