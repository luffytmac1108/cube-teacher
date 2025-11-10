package com.yxw.cube.teacher.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class OutputUser {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("部门")
    private String department;

    // 从Word中获取的字段
    @ExcelProperty("户籍地址")
    private String address;

    @ExcelProperty("个人喜好")
    private String preference;
}
