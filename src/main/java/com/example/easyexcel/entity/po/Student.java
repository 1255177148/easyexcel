package com.example.easyexcel.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Date 2020/8/7 9:54
 * @Author hezhan
 */
@Data
@TableName("student")
public class Student {

    @TableId
    private String id;
    private String name;
}
