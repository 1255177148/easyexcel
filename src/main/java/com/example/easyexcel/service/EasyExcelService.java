package com.example.easyexcel.service;

import com.example.easyexcel.entity.excel.TeacherExcel;

import java.util.List;

public interface EasyExcelService {

    /**
     * 保存数据到数据库中
     * @param list 要保存的数据
     * @param batchSize 每批保存多少条
     */
    void saveTeacher(List<TeacherExcel> list, int batchSize);
}
