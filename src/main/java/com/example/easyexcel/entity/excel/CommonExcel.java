package com.example.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

/**
 * 通用excel导入实体类父类
 */
@Data
public class CommonExcel {

    /**
     * 行号
     */
    @ExcelIgnore
    private String num;
}
