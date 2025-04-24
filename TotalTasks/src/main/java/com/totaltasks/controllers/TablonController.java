package com.totaltasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TablonController {


    @GetMapping("/crearTablon")
    public String crearTablon() {
        return "";
    }
    
}