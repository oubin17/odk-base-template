package com.odk.baseweb;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.HelloWorldApi;
import com.odk.baseutil.request.HelloWorldRequest;
import com.odk.baseutil.response.HelloWorldResponse;
import org.springframework.web.bind.annotation.*;

/**
 * HelloWorldController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@RestController
@RequestMapping("/hello")
public class HelloWorldController {


    private final HelloWorldApi helloWorldApi;

    public HelloWorldController(HelloWorldApi helloWorldApi) {
        this.helloWorldApi = helloWorldApi;
    }

    @GetMapping
    public ServiceResponse<HelloWorldResponse> helloWorld() {
        HelloWorldResponse response = new HelloWorldResponse();
        response.setResult("调用到服务端啦");

        return ServiceResponse.valueOfSuccess(response);
    }

    @PostMapping
    public ServiceResponse<HelloWorldResponse> helloWorldPost(@RequestBody HelloWorldRequest helloWorldRequest) {
        return helloWorldApi.helloWorld(helloWorldRequest);
    }

}
