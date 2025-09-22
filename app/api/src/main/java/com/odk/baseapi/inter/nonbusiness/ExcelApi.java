package com.odk.baseapi.inter.nonbusiness;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;

/**
 * ExcelApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
public interface ExcelApi {

    ServiceResponse<ExcelUploadResponse> upload(ExcelUploadRequest excelUploadRequest);

}
