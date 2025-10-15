package com.yxw.cube.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CubeTeacherController {

    @GetMapping("/cube21")
    public String cube21() {
        return "cube21";
    }
}