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
        return notificacionUsuarioRepository.findByDestinatarioIdUsuarioAndVistaFalseOrderByNotificacion_FechaCreacionDesc(userId);
    }

    @Override
    public void actualizarEstadoNoti(Long idUsuarioNoti) {
        NotificacionUsuarioEntity notificacionUsuarioEntity = notificacionUsuarioRepository.findById(idUsuarioNoti).orElse(null);
        notificacionUsuarioEntity.setVista(true);
        notificacionUsuarioRepository.save(notificacionUsuarioEntity);
    }

    @Override
    public void leerTodasLasNotis(Long idUsuario) {
        List<NotificacionUsuarioEntity> notificacionesNoLeidas = notificacionesNoLeidasPorUserId(idUsuario);

        for (int i = 0; i < notificacionesNoLeidas.size(); i++) {
            notificacionesNoLeidas.get(i).setVista(true);
            notificacionUsuarioRepository.save(notificacionesNoLeidas.get(i));
        }
    }

}