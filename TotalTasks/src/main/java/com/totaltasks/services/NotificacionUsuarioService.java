package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.NotificacionUsuarioEntity;

public interface NotificacionUsuarioService {

    List<NotificacionUsuarioEntity> notificacionesNoLeidasPorUserId(Long userId);
}
