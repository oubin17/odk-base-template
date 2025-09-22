package com.odk.baseutil.response;

import com.odk.base.vo.response.BaseResponse;
import com.odk.baseutil.dto.excel.ExcelRowDTO;
import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * ExcelUploadResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@Data
public class ExcelUploadResponse extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 371480163774893081L;

    private List<ExcelRowDTO> excelRowDTOS;

}
