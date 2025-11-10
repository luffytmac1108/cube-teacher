package com.yxw.cube.teacher.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class InputUser {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("部门")
    private String department;
}