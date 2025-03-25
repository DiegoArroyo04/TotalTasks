package com.totaltasks.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.UsuarioService;

@Service
public class UsuarioServiceImplementation implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public String registrarUsuario(UsuarioDTO usuario) {

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        // REVISON DE QUE EL USUARIO NO EXISTA
        if (usuarioRepository.findByusuario(usuario.getUsuario()) != null) {
            return "Ya existe un usuario registrado con ese nombre de usuario.";
        } else if (usuarioRepository.findByemail(usuario.getEmail()) != null) {
            return "Ya existe un usuario registrado con ese email.";
        } else {

            // CONVERTIR A ENTITY Y GUARDAR EN BBDD
            usuarioEntity.setNombre(usuario.getNombre());
            usuarioEntity.setUsuario(usuario.getUsuario());
            usuarioEntity.setEmail(usuario.getEmail());
            usuarioEntity.setContrasenia(usuario.getContrasenia());
            usuarioRepository.save(usuarioEntity);
            return "Cuenta creada con Éxito.";
        }

    }

    @Override
    public String comprobarLogin(UsuarioDTO usuario) {

        // BUSCAR USUARIO
        UsuarioEntity usuarioEntity = usuarioRepository.findByusuario(usuario.getUsuario());

        if (usuarioEntity == null) {
            return "Usuario no encontrado.Por favor,registrese.";
        } else {

            // COMPROBAR QUE LAS CONTRASEÑAS COINCIDAN
            if (usuarioEntity.getContrasenia().equals(usuario.getContrasenia())) {
                return "Encontrado";
            } else {
                return "La contraseña no coincide.Por favor,vuelve a intentarlo.";
            }

        }

    }

    @Override
    public UsuarioEntity encontrarUsuario(String usuario) {
        // BUSCAR USUARIO
        return usuarioRepository.findByusuario(usuario);

    }

}
