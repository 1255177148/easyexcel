package com.example.easyexcel.controller;

import com.example.easyexcel.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author hezhan
 * @Date 2023/8/20 16:28
 **/
@RestController
@RequestMapping("/api/init")
public class InitDataController {

    @Resource
    private StudentService studentService;

    @GetMapping("studentData")
    public void initStudentDate(){
        studentService.initStudentData();
    }
}
