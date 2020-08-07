package com.example.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Date 2020/8/7 9:58
 * @Author hezhan
 */
@Data
public class StudentExcel {
    @ExcelProperty(value = "姓名", index = 0)
    private String name;
}
