package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.totaltasks.services.NotificacionUsuarioService;

@Controller
public class NotificacionController {

    @Autowired
    NotificacionUsuarioService notificacionUsuarioService;

    @PostMapping("/leerNotificacion")
    public String leerNotificacion(@RequestParam Long idUsuarioNoti) {
        notificacionUsuarioService.actualizarEstadoNoti(idUsuarioNoti);
        return "redirect:/dashboard";
    }

    @PostMapping("/leerTodasLasNotis")
    public String leerTodasLasNotis(@RequestParam Long idUsuario) {
        notificacionUsuarioService.leerTodasLasNotis(idUsuario);
        return "redirect:/dashboard";
    }

}
