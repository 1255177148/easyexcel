package com.example.easyexcel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyexcel.entity.po.Student;
import com.example.easyexcel.mapper.StudentMapper;
import com.example.easyexcel.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @Date 2020/8/7 9:57
 * @Author hezhan
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
