package com.example.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.easyexcel.entity.po.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Date 2020/8/7 9:58
 * @Author hezhan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentExcel extends CommonExcel {
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "性别", index = 1)
    private String sex;

    @ExcelProperty(value = "成绩", index = 2)
    private String score;

    @ExcelProperty(value = "创建时间", index = 3)
    private String createTime;

    @ExcelProperty(value = "班级", index = 4)
    private String className;

    @ExcelProperty(value = "教师名称", index = 5)
    private String teacherName;

    @ExcelProperty(value = "年龄", index = 6)
    private Integer age;

    public static List<StudentExcel> valueOf(List<Student> students){
        if (CollectionUtils.isEmpty(students)){
            return Collections.emptyList();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<StudentExcel> excels = new ArrayList<>();
        students.forEach(s -> {
            StudentExcel excel = new StudentExcel();
            excel.setName(s.getName());
            excel.setAge(s.getAge());
            excel.setClassName(s.getClassName());
            if (s.getScore() != null){
                excel.setScore(s.getScore().toPlainString());
            }
            excel.setSex(s.getSex() == 0 ? "女" : "男");
            excel.setCreateTime(format.format(s.getCreateTime()));
            excel.setTeacherName(s.getTeacherName());
            excels.add(excel);
        });
        return excels;
    }
}
