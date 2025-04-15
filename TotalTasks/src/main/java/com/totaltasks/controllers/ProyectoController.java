package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.services.ProyectoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping("/crearProyectoDesdeRepo")
    public String crearProyectoDesdeRepo(@RequestParam String repoName, @RequestParam String repoDescription, @RequestParam String metodologia,
    HttpSession session) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        // Crear el ProyectoEntity y asignarle valores
        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setNombreProyecto(repoName);
        proyecto.setDescripcion(repoDescription);
        proyecto.setMetodologia(metodologia);
        proyecto.setCreador(usuario);
    
        // Guardar el proyecto
        proyectoService.guardarProyecto(proyecto);
        
        // Redireccionar a dashboard
        return "redirect:/dashboard";
    }
    
}