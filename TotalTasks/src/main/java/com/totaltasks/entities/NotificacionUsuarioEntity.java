package com.totaltasks.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notificacion_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion_usuario", unique = true, nullable = false)
    private Long idNotificacionUsuario;

    @ManyToOne
    @JoinColumn(name = "id_notificacion", nullable = false)
    private NotificacionEntity notificacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_destinatario", nullable = false)
    private UsuarioEntity destinatario;

    @Column(name = "vista", nullable = false)
    private boolean vista = false;
}
