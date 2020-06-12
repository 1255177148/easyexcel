package com.example.easyexcel.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author Zhanzhan
 * @Date 2020/6/6 17:28
 */
@Data
@TableName("teacher")
public class Teacher {

    @TableId
    private String id;
    private String name;
    private int sex;
    private String position;
    private int number;
}
