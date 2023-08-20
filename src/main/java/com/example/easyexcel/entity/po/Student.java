package com.example.easyexcel.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Date 2020/8/7 9:54
 * @Author hezhan
 */
@Data
@TableName("student")
public class Student {

    @TableId
    private String id;

    @TableField("name")
    private String name;

    @TableField("sex")
    private Integer sex;

    @TableField("score")
    private BigDecimal score;

    @TableField("create_time")
    private Date createTime;

    @TableField("class_name")
    private String className;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("age")
    private Integer age;
}
