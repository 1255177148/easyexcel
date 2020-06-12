package com.example.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.listen.CommonListen;
import com.example.easyexcel.service.EasyExcelService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Author hezhan
 * @Date 2020/6/12 9:59
 * excel数据service类
 */
@Service
public class EasyExcelServiceImpl implements EasyExcelService {

    @Override
    public void importData(InputStream inputStream) {
        //todo 可以使用策略模式，这样可以处理多种excel保存到数据库
        EasyExcel.read(inputStream, TeacherExcel.class, new CommonListen<Teacher>(Teacher.class, "saveTeacher"));
    }
}
