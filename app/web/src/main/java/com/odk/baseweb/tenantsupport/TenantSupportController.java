package com.odk.baseweb.tenantsupport;

import com.odk.baseweb.interceptor.tenantid.SupportTenantId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TenantSupportController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/18
 */
@SupportTenantId("DEFAULT")
@RestController
@RequestMapping("/tenant")
public class TenantSupportController {

    @RequestMapping("/support")
    public String support() {
        return "support";
    }
}
