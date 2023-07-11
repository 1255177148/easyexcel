package com.example.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.exception.CustomException;
import com.example.easyexcel.listen.CommonListen;
import com.example.easyexcel.service.EasyExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Date 2020/8/6 10:25
 * @Author hezhan
 */
@Api(value = "测试excel导入导出", tags = "测试excel导入导出")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private EasyExcelService easyExcelService;

    @ApiOperation(value = "excel文件上传", notes = "excel文件上传", httpMethod = "POST")
    @ApiImplicitParam(name = "file", value = "excel文件", dataType = "__file", paramType = "form")
    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(), TeacherExcel.class, new CommonListen<TeacherExcel>("saveTeacher")).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException("将上传的文件转为输入流时报错");
        }
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response){
        easyExcelService.outputTeacherData(response);
    }

    @GetMapping("/downloads")
    public void downloads(HttpServletResponse response){
        easyExcelService.downloadToSheets(response);
    }

    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response){
        easyExcelService.downloadTemplate(response);
    }
}
