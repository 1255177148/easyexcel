package com.example.easyexcel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyexcel.entity.po.Student;
import com.example.easyexcel.mapper.StudentMapper;
import com.example.easyexcel.service.StudentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Date 2020/8/7 9:57
 * @Author hezhan
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public void initStudentData() {
        List<Student> students = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < 500000; i++){
            Student student = new Student();
            student.setName("学生" + i);
            student.setAge(15);
            student.setClassName("班级1");
            student.setScore(new BigDecimal("60.87"));
            student.setSex(i % 2);
            student.setCreateTime(now);
            student.setTeacherName("教师" + i);
            students.add(student);
        }
        saveBatch(students);
    }
}
