package com.example.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author Zhanzhan
 * @Date 2020/6/6 17:22
 */
@Data
public class TeacherExcel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "性别", index = 1)
    private Integer sex;

    @ExcelProperty(value = "职位", index = 2)
    private String position;

    @ExcelProperty(value = "编号", index = 3)
    private Integer number;
}
