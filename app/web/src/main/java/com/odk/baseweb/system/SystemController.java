package com.odk.baseweb.system;

import com.odk.base.vo.response.ServiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/7
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    /**
     * 验证服务是否正常
     *
     * @return
     */
    @GetMapping("/validateToken")
    public ServiceResponse<String> validateToken() {
        return ServiceResponse.valueOfSuccess();
    }
}
