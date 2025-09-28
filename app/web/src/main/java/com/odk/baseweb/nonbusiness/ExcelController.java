package com.odk.baseweb.nonbusiness;

import cn.dev33.satoken.annotation.SaIgnore;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.nonbusiness.ExcelApi;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @SaIgnore
    @PostMapping("/export")
    public ResponseEntity<Resource> export() {
        ServiceResponse<Resource> export = excelApi.export();
        Resource resource = export.getData();

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_info.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @Autowired
    public void setExcelApi(ExcelApi excelApi) {
        this.excelApi = excelApi;
    }
}
