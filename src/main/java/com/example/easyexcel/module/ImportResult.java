package com.example.easyexcel.module;

import lombok.Data;

import java.util.List;

/**
 * 上传完成后返回的结果
 */
@Data
public class ImportResult {

    /**
     * 上传的总条数
     */
    private String total;

    /**
     * 成功的总条数
     */
    private String successTotal;

    /**
     * 失败的总条数
     */
    private String failTotal;

    /**
     * 错误提示
     */
    private List<ImportTips> tips;

    /**
     * 模板错误提示
     */
    private String errorMessage;
}
