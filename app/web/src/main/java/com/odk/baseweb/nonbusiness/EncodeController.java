package com.odk.baseweb.nonbusiness;

import cn.dev33.satoken.annotation.SaIgnore;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.EncodeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * EncodeController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
@RestController
@RequestMapping("/encode")
public class EncodeController {

    private EncodeApi encodeApi;

    @SaIgnore
    @GetMapping("/public")
    public ServiceResponse<String> addPermission(@RequestParam("rawData") String rawData) {
        return this.encodeApi.publicKeyEncode(rawData);
    }


    @Autowired
    public void setEncodeApi(EncodeApi encodeApi) {
        this.encodeApi = encodeApi;
    }
}
