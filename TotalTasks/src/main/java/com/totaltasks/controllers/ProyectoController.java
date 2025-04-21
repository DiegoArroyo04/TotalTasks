package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.services.ProyectoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping("/crearProyectoManualmente")
    public String crearProyectoManualmente(Model model ,@RequestParam String nombre, @RequestParam String descripcion, @RequestParam String metodologia,
    HttpSession session) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        ProyectoEntity proyectoExistente = proyectoService.obtenerProyectoPorNombre(nombre);

        if (proyectoExistente != null) {
            model.addAttribute("error", "Ya existe un proyecto con ese nombre.");
            return "redirect:/dashboard";
        }

        ProyectoDTO proyecto = new ProyectoDTO();
        proyecto.setNombreProyecto(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setMetodologia(metodologia);
        proyecto.setIdCreador(usuario.getIdUsuario());

        proyectoService.guardarProyecto(proyecto);

        return "redirect:/dashboard";
    }

    @PostMapping("/crearProyectoDesdeRepo")
    public String crearProyectoDesdeRepo(Model model, @RequestParam String repoName, @RequestParam String repoDescription, @RequestParam String metodologia,
    HttpSession session) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");
        
        if (usuario == null) {
            return "redirect:/login";
        }

        ProyectoEntity proyectoExistente = proyectoService.obtenerProyectoPorNombre(repoName);

        if (proyectoExistente != null) {
            model.addAttribute("error", "Ya existe un proyecto con ese nombre.");
            return "redirect:/dashboard";
        }
        
        ProyectoDTO proyecto = new ProyectoDTO();
        proyecto.setNombreProyecto(repoName);
        proyecto.setDescripcion(repoDescription);
        proyecto.setMetodologia(metodologia);
        proyecto.setIdCreador(usuario.getIdUsuario());
    
        proyectoService.guardarProyecto(proyecto);
        
        return "redirect:/dashboard";
    }

    @PostMapping("/unirseProyecto")
    public String unirseAProyecto(@RequestParam String codigoProyecto, HttpSession session, RedirectAttributes redirectAttributes) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        boolean unido = proyectoService.unirseAProyectoPorCodigo(codigoProyecto, usuario);

        if (unido) {
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addAttribute("error", "No se ha encontrado el proyecto");
            return "redirect:/dashboard";
        }
    }
 
}