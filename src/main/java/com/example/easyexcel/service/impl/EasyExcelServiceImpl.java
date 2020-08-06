package com.example.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.exception.CustomException;
import com.example.easyexcel.service.EasyExcelService;
import com.example.easyexcel.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author hezhan
 * @Date 2020/6/12 9:59
 * excel数据service类
 */
@Service
public class EasyExcelServiceImpl implements EasyExcelService {

    @Autowired
    private TeacherService teacherService;

    @Override
    public void saveTeacher(List<TeacherExcel> list, int batchSize) {
        if (CollectionUtils.isEmpty(list))
            return;
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher = null;
        for (TeacherExcel teacherExcel : list) {
            teacher = new Teacher();
            teacher.setId(UUID.randomUUID().toString());
            teacher.setName(teacherExcel.getName());
            teacher.setNumber(teacherExcel.getNumber());
            teacher.setPosition(teacherExcel.getPosition());
            teacher.setSex(teacherExcel.getSex());
            teachers.add(teacher);
        }
        teacherService.saveBatch(teachers, batchSize);
    }

    @Override
    public void outputTeacherData(HttpServletResponse response) {
        List<Teacher> teachers = teacherService.list();
        if (CollectionUtils.isEmpty(teachers))
            return;
        List<TeacherExcel> list = new ArrayList<>();
        TeacherExcel teacherExcel = null;
        for (Teacher teacher : teachers) {
            teacherExcel = new TeacherExcel();
            teacherExcel.setName(teacher.getName());
            teacherExcel.setNumber(teacher.getNumber());
            teacherExcel.setPosition(teacher.getPosition());
            teacherExcel.setSex(teacher.getSex());
            list.add(teacherExcel);
        }
        // 直接通过response导出到浏览器时会默认下载成一个zip压缩文件而不是excel表格，需要在response header头里加以下几个参数
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = null;
        try {
            fileName = URLEncoder.encode("测试", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException("url encode时报错");
        }
        // 定义浏览器下载时excel表的名字
        response.setHeader("Content-disposition", "attachment;filename*= UTF-8''" + fileName + ".xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), TeacherExcel.class).sheet("测试").doWrite(list);
        } catch (IOException e) {
            throw new CustomException("导出为excel时报错，报错原因为：" + e.getCause());
        }
    }
}
