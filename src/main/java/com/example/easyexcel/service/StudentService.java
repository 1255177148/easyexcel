package com.example.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.easyexcel.entity.po.Student;

/**
 * @Date 2020/8/7 9:56
 * @Author hezhan
 */
public interface StudentService extends IService<Student> {

    /**
     * 初始化50万条数据进数据库
     */
    void initStudentData();
}
