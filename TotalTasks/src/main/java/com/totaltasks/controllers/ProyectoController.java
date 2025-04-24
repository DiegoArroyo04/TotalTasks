package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;
import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TablonService tablonService;

    @PostMapping("/crearProyectoManualmente")
    public String crearProyectoManualmente(RedirectAttributes redirectAttributes, @RequestParam String nombre,
            @RequestParam String descripcion, @RequestParam String metodologia,
            HttpSession session) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        ProyectoEntity proyectoExistente = proyectoService.obtenerProyectoPorNombre(nombre);

        if (proyectoExistente != null) {
            redirectAttributes.addAttribute("error", "El proyecto ya existe");
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
    public String crearProyectoDesdeRepo(RedirectAttributes redirectAttributes, @RequestParam String repoName,
            @RequestParam String repoDescription, @RequestParam String metodologia,
            HttpSession session) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        ProyectoEntity proyectoExistente = proyectoService.obtenerProyectoPorNombre(repoName);

        if (proyectoExistente != null) {
            redirectAttributes.addAttribute("error", "El proyecto ya existe");
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
    public String unirseAProyecto(@RequestParam String codigoProyecto, HttpSession session,
            RedirectAttributes redirectAttributes) {

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

    // REDIRECCION PROYECTOS
    @GetMapping("/proyecto/{id}")
    public String verProyecto(@PathVariable Long id, Model model, HttpSession session) {

        ProyectoEntity proyecto = proyectoService.obtenerProyectoPorId(id);
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (proyecto == null) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("proyecto", proyecto);
            model.addAttribute("usuario", session.getAttribute("usuario"));
            if (proyecto.getMetodologia().equals("Kanban")) {

                model.addAttribute("tablones", tablonService.ordenarTablones(proyecto.getTablones()));
                model.addAttribute("tareas", proyecto.getTareas());
                model.addAttribute("usuario", usuario);
                model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));

                // FOTO DE PERFIL DE GOOGLE Y GITHUB
                model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
                model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));

                model.addAttribute("paginaActual", "proyecto");

                return "proyectos/proyectoKanban";
            } else if (proyecto.getMetodologia().equals("Scrum")) {
                return "proyectos/proyectoScrum";
            } else {
                return "proyectoXP";
            }
        }

    }

    @PostMapping("/proyecto/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id, @RequestParam boolean abandonar) {
        proyectoService.deleteById(id, abandonar);
        return "redirect:/dashboard";
    }

}