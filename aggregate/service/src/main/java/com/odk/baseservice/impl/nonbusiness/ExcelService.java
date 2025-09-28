package com.odk.baseservice.impl.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.ExcelApi;
import com.odk.basemanager.nonbusiness.ExcelUploadManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * ExcelService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@Service
@AllArgsConstructor
public class ExcelService extends AbstractApiImpl implements ExcelApi {

    private ExcelUploadManager excelUploadManager;

    @Override
    public ServiceResponse<ExcelUploadResponse> upload(ExcelUploadRequest excelUploadRequest) {
        return super.bizProcess(BizScene.EXCEL_UPLOAD, excelUploadRequest, new AbstractApiImpl.ApiCallBack<ExcelUploadResponse, ExcelUploadResponse>() {

            @Override
            protected ExcelUploadResponse doProcess(Object args) {
                return excelUploadManager.upload(excelUploadRequest);
            }

            @Override
            protected ExcelUploadResponse convertResult(ExcelUploadResponse result) {
                return result;
            }

        });
    }

    @Override
    public ServiceResponse<Resource> export() {
        return super.bizProcess(BizScene.EXCEL_DOWNLOAD, null, new AbstractApiImpl.ApiCallBack<Resource, Resource>() {

            @Override
            protected Resource doProcess(Object args) {
                return excelUploadManager.export();
            }

            @Override
            protected Resource convertResult(Resource result) {
                return result;
            }

        });
    }
}
