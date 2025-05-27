package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.totaltasks.services.NotificacionUsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NotificacionController {

    @Autowired
    NotificacionUsuarioService notificacionUsuarioService;

    @PostMapping("/leerNotificacion")
    public String leerNotificacion(@RequestParam Long idUsuarioNoti, HttpServletRequest request) {
        notificacionUsuarioService.actualizarEstadoNoti(idUsuarioNoti);
        // REDIRECCION A LA ULTIMA RUTA DISPONIBLE
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }

    @PostMapping("/leerTodasLasNotis")
    public String leerTodasLasNotis(@RequestParam Long idUsuario, HttpServletRequest request) {
        notificacionUsuarioService.leerTodasLasNotis(idUsuario);
        // REDIRECCION A LA ULTIMA RUTA DISPONIBLE
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }

}