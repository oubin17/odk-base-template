package com.odk.baseweb.nonbusiness;

import cn.dev33.satoken.annotation.SaIgnore;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.ExcelApi;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ExcelController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    private ExcelApi excelApi;

    /**
     * 解析 excel
     *
     * @param file
     * @return
     */
    @SaIgnore
    @PostMapping("/upload")
    ServiceResponse<ExcelUploadResponse> compare(MultipartFile file) throws IOException {
        ExcelUploadRequest uploadRequest = new ExcelUploadRequest();
        uploadRequest.setFileInputStream(file.getInputStream());
        uploadRequest.setFileSize(file.getSize() + "");
        uploadRequest.setFileName(file.getOriginalFilename());
        return this.excelApi.upload(uploadRequest);
    }

    @Autowired
    public void setExcelApi(ExcelApi excelApi) {
        this.excelApi = excelApi;
    }
}
