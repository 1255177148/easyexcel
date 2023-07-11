package com.example.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.easyexcel.entity.excel.StudentExcel;
import com.example.easyexcel.entity.excel.TeacherExcel;
import com.example.easyexcel.entity.po.Student;
import com.example.easyexcel.entity.po.Teacher;
import com.example.easyexcel.exception.CustomException;
import com.example.easyexcel.service.EasyExcelService;
import com.example.easyexcel.service.StudentService;
import com.example.easyexcel.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
@Slf4j
public class EasyExcelServiceImpl implements EasyExcelService {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

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
        List<TeacherExcel> list = build();
        buildExcelName(response, "测试", false);
        try {
            EasyExcel.write(response.getOutputStream(), TeacherExcel.class).sheet("测试").doWrite(list);
        } catch (IOException e) {
            throw new CustomException("导出为excel时报错，报错原因为：" + e.getCause());
        }

    }

    @Override
    public void downloadToSheets(HttpServletResponse response) {
        List<TeacherExcel> list = build();
        List<Student> studentList = studentService.list();
        buildExcelName(response, "多个sheet测试", false);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            throw new CustomException("构建ExcelWriter时报错，报错原因为：" + e.getCause());
        }
        // 指定sheet和用哪个类来写
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "表1").head(TeacherExcel.class).build();
        excelWriter.write(list, writeSheet);
        if (!CollectionUtils.isEmpty(studentList)){
            List<StudentExcel> studentExcels = new ArrayList<>();
            StudentExcel studentExcel = null;
            for (Student student : studentList){
                studentExcel = new StudentExcel();
                studentExcel.setName(student.getName());
                studentExcels.add(studentExcel);
            }
            writeSheet = EasyExcel.writerSheet(1, "表2").head(StudentExcel.class).build();
            excelWriter.write(studentExcels, writeSheet);
        }
        // 最后关闭流
        excelWriter.finish();
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        InputStream fileInputStream = null;
        InputStream fis = null;
        OutputStream outputStream = null;
        String fileName = "矾山人员导入模板";
        try {
            // 将文件写入输入流
            //主要ClassPathResource是获取类路径下的文件
            File file = new File("/home/template/" + fileName + ".xlsx");
            fileInputStream = new FileInputStream(file);
            fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            buildExcelName(response, fileName, false);
            // 告知浏览器文件的大小
            // response.addHeader("Content-Length", "" + fileInputStream.length());
            outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件下载异常");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<TeacherExcel> build(){
        List<Teacher> teachers = teacherService.list();
        if (CollectionUtils.isEmpty(teachers))
            return new ArrayList<>();
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
        return list;
    }

    /**
     * 构建导出的excel表的名字
     * @param response http请求响应对象
     * @param name 要导出的excel表文件的名称
     * @param isXls 导出的excel表的格式是否为xls格式
     */
    private void buildExcelName(HttpServletResponse response, String name, boolean isXls){
        // 直接通过response导出到浏览器时会默认下载成一个zip压缩文件而不是excel表格，需要在response header头里加以下几个参数
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        String fileName = null;
        try {
            fileName = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException("url encode时报错");
        }
        // 定义浏览器下载时excel表的名字
        if (isXls){
            response.setHeader("Content-disposition", "attachment;filename*= UTF-8''" + fileName + ".xls");
        } else {
            response.setHeader("Content-disposition", "attachment;filename*= UTF-8''" + fileName + ".xlsx");
        }
    }
}
