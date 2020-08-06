package com.example.easyexcel.service.impl;

import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.service.EasyExcelService;
import com.example.easyexcel.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        for (TeacherExcel teacherExcel : list){
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
}
