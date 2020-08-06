package com.example.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.exception.CustomException;
import com.example.easyexcel.listen.CommonListen;
import com.example.easyexcel.service.EasyExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Date 2020/8/6 10:25
 * @Author hezhan
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private EasyExcelService easyExcelService;

    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(), TeacherExcel.class, new CommonListen<Teacher>(easyExcelService, "saveTeacher")).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException("将上传的文件转为输入流时报错");
        }
    }
}
