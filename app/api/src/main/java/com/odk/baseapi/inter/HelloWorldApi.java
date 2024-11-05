package com.odk.baseapi.inter;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.HelloWorldRequest;
import com.odk.baseutil.response.HelloWorldResponse;

/**
 * HelloWorldApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface HelloWorldApi {


    ServiceResponse<HelloWorldResponse> helloWorld(HelloWorldRequest helloWorldRequest);

}
