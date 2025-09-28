package com.odk.baseutil.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ExcelRowDTO 这里最好都设置好 index，如果不设置index，会按照字段顺序读取
 * 优先级：@ExcelProperty 注解中的 index > @ExcelProperty 中的 value > 字段声明顺序
 * 名称匹配：如果使用名称映射，Excel 中的列标题必须与 @ExcelProperty 注解中的值完全匹配
 * 类型转换：EasyExcel 会自动进行基本类型转换（String、Integer、Double、Date 等）
 * 日期处理：可以使用 @DateTimeFormat 注解指定日期格式
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelRowDTO extends DTO {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "手机号", index = 1)
    private String mobile;

    @ExcelProperty(value = "性别", index = 2)
    private String gender;

    @ExcelProperty(value = "年龄", index = 3)
    private String age;

    @ExcelProperty(value = "身份证", index = 4)
    private String idNo;



}
