package com.example.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Date 2020/8/7 9:58
 * @Author hezhan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentExcel extends CommonExcel {
    @ExcelProperty(value = "姓名", index = 0)
    private String name;
}
