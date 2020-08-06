package com.example.easyexcel.service;

import com.example.easyexcel.entity.excel.TeacherExcel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EasyExcelService {

    /**
     * 保存数据到数据库中
     * @param list 要保存的数据
     * @param batchSize 每批保存多少条
     */
    void saveTeacher(List<TeacherExcel> list, int batchSize);

    /**
     * 从数据库中获取数据并导出
     * @param response
     */
    void outputTeacherData(HttpServletResponse response);
}
