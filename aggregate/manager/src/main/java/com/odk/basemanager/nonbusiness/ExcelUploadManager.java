package com.odk.basemanager.nonbusiness;

import com.odk.base.util.ExcelReadUtil;
import com.odk.baseutil.dto.excel.ExcelRowDTO;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ExcelUploadManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@Slf4j
@Service
public class ExcelUploadManager {

    public ExcelUploadResponse upload(ExcelUploadRequest excelUploadRequest) {
        List<ExcelRowDTO> excelRowDTOS = ExcelReadUtil.readExcel(excelUploadRequest.getFileInputStream(), ExcelRowDTO.class);


//        ExcelReadUtil.readExcelSingleDeal(excelUploadRequest.getFileInputStream(), ExcelRowDTO.class, 0, row -> {
//            log.info("row:{}", row);
//        });

//        ExcelReadUtil.readExcelBatchDeal(excelUploadRequest.getFileInputStream(), ExcelRowDTO.class, 0, 4, rows -> {
//            log.info("rows:{}", rows);
//        });

//        List<ExcelRowDTO> excelRowDTOS = ExcelReadUtil.readExcelIgnoreHead(excelUploadRequest.getFileInputStream(), ExcelRowDTO.class, 0, 2);
//        log.info("excelRowDTOS:{}", excelRowDTOS);
        ExcelUploadResponse excelUploadResponse = new ExcelUploadResponse();
        excelUploadResponse.setExcelRowDTOS(excelRowDTOS);
        return excelUploadResponse;
    }
}
