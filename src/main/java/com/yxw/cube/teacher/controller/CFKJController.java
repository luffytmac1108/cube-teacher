package com.yxw.cube.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CFKJController {

    @GetMapping("/cfkj")
    public String cfkj() {
        return "cfkj9";
    }
}