package com.odk.basemanager.nonbusiness;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.odk.base.util.ExcelReadUtil;
import com.odk.base.util.ExcelWriteUtil;
import com.odk.baseutil.dto.excel.ExcelRowDTO;
import com.odk.baseutil.request.excel.ExcelUploadRequest;
import com.odk.baseutil.response.ExcelUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

    public Resource export() {

        List<ExcelRowDTO> excelRowDTOS = new ArrayList<>();
        ExcelRowDTO excelRowDTO = new ExcelRowDTO();
        excelRowDTO.setName("张三");
        excelRowDTO.setMobile("13888888888");
        excelRowDTO.setGender("男");
        excelRowDTO.setAge("18");
        excelRowDTO.setIdNo("420000000000000000");

        ExcelRowDTO excelRowDTO2 = new ExcelRowDTO();
        excelRowDTO2.setName("李四");
        excelRowDTO2.setMobile("13888888888");
        excelRowDTO2.setGender("女");
        excelRowDTO2.setAge("18");
        excelRowDTO2.setIdNo("420000000000000000");
        excelRowDTOS.add(excelRowDTO);
        excelRowDTOS.add(excelRowDTO2);
        try {
            ByteArrayOutputStream outputStream = writeToStreamExample(excelRowDTOS);
            // ... 写入完成后可以获取字节数组
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Excel数据写入到ByteArrayOutputStream，以便通过HTTP响应返回
     */
    public static ByteArrayOutputStream writeToStreamExample(List<ExcelRowDTO> excelRowDTOS) throws Exception {
        // 使用ByteArrayOutputStream替代FileOutputStream
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 使用工具类写入Excel到内存流
            ExcelWriteUtil.writeExcel(excelRowDTOS, ExcelRowDTO.class, outputStream, "用户信息");
            return outputStream;
        }
        // 关闭流
    }

    /**
     * 写入到文件的示例，直接写文件到本地
     */
    public static OutputStream writeToFileExample(List<ExcelRowDTO> excelRowDTOS) throws Exception {
        // 创建文件输出流 - 这就是 outputStream 的来源
        OutputStream outputStream = new FileOutputStream("user_data.xlsx");
        try {
            // 使用工具类写入Excel
            ExcelWriteUtil.writeExcel(excelRowDTOS, ExcelRowDTO.class, outputStream, "用户信息");
            return outputStream;
        } finally {
            // 关闭流
            outputStream.close();
        }
    }

    /**
     * 流式写入大数据量的示例，写到本地
     */
    public void writeLargeDataExample() throws Exception {
        // 创建文件输出流
        OutputStream outputStream = new FileOutputStream("large_data.xlsx");

        ExcelWriter excelWriter = null;
        try {
            // 创建Excel写入器
            excelWriter = ExcelWriteUtil.createExcelWriter(ExcelRowDTO.class, outputStream, "大数据");
            WriteSheet writeSheet = ExcelWriteUtil.createWriteSheet(excelWriter, "用户数据");

            // 模拟循环从数据库读取大量数据并分批写入
            int totalRecords = 100000; // 总记录数
            int batchSize = 1000;       // 每批处理1000条

            for (int i = 0; i < totalRecords; i += batchSize) {
                // 模拟从数据库读取一批数据
                List<ExcelRowDTO> batchData = readBatchDataFromDatabase(i, Math.min(batchSize, totalRecords - i));

                // 写入这一批数据
                ExcelWriteUtil.writeDataToSheet(excelWriter, writeSheet, batchData);

                System.out.println("已处理 " + (i + batchData.size()) + " 条记录");
            }

        } finally {
            // 完成写入并关闭资源
            ExcelWriteUtil.finishExcelWriter(excelWriter);
            outputStream.close();
        }

        System.out.println("大数据Excel文件写入完成");
    }

    private List<ExcelRowDTO> readBatchDataFromDatabase(int startIndex, int batchSize) {
        List<ExcelRowDTO> excelRowDTOS = new ArrayList<>();
        ExcelRowDTO excelRowDTO = new ExcelRowDTO();
        excelRowDTO.setName("张三");
        excelRowDTO.setMobile("13888888888");
        excelRowDTO.setGender("男");
        excelRowDTO.setAge("18");
        excelRowDTO.setIdNo("420000000000000000");

        ExcelRowDTO excelRowDTO2 = new ExcelRowDTO();
        excelRowDTO2.setName("李四");
        excelRowDTO2.setMobile("13888888888");
        excelRowDTO2.setGender("女");
        excelRowDTO2.setAge("18");
        excelRowDTO2.setIdNo("420000000000000000");
        excelRowDTOS.add(excelRowDTO);
        excelRowDTOS.add(excelRowDTO2);

        return excelRowDTOS;

    }


}
