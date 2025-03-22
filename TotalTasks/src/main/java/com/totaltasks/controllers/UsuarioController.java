package com.totaltasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    @GetMapping("/registro")
    public String index() {
        return "registro";
    }
}
