package com.odk.baseutil.request.excel;

import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;
import java.io.Serial;

/**
 * ExcelUploadRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExcelUploadRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 4220899554387439409L;

    private InputStream fileInputStream;

    private String fileName;

    private String fileSize;
}
