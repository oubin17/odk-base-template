package com.odk.baseservice;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.HelloWorldRequest;
import com.odk.baseutil.response.HelloWorldResponse;

/**
 * HelloWorldService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface HelloWorldService {

    ServiceResponse<HelloWorldResponse> helloWorld(HelloWorldRequest dto);
}
