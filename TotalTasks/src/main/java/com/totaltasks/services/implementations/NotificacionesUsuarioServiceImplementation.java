package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.NotificacionUsuarioEntity;
import com.totaltasks.repositories.NotificacionUsuarioRepository;
import com.totaltasks.services.NotificacionUsuarioService;

@Service
public class NotificacionesUsuarioServiceImplementation implements NotificacionUsuarioService {

    @Autowired
    private NotificacionUsuarioRepository notificacionUsuarioRepository;

    @Override
    public List<NotificacionUsuarioEntity> notificacionesNoLeidasPorUserId(Long userId) {
        return notificacionUsuarioRepository.findByDestinatarioIdUsuarioAndVistaFalse(userId);
    }

}
