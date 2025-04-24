package com.odk.baseservice.impl.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.EncodeApi;
import com.odk.basemanager.deal.nonbusiness.EncodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EncodeService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
@Service
public class EncodeService extends AbstractApiImpl implements EncodeApi {

    private EncodeManager encodeManager;

    @Override
    public ServiceResponse<String> publicKeyEncode(String rawData) {
        return super.bizProcess(BizScene.ENCODE_DATA, rawData, new AbstractApiImpl.ApiCallBack<String, String>() {

            @Override
            protected String doProcess(Object args) {
                return encodeManager.encode(rawData);
            }

            @Override
            protected String convertResult(String result) {
                return result;
            }

        });
    }

    @Autowired
    public void setEncodeManager(EncodeManager encodeManager) {
        this.encodeManager = encodeManager;
    }
}
