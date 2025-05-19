package com.totaltasks.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario", unique = true, nullable = false)
	private Long idUsuario;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "usuario", unique = true, nullable = false)
	private String usuario;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "contrasenia", nullable = true)
	private String contrasenia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", nullable = false, insertable = false)
	private Timestamp fechaCreacion;

	@Lob
	@Column(name = "foto_perfil", nullable = true)
	private byte[] fotoPerfil;

	@OneToMany(mappedBy = "creador")
	private List<ProyectoEntity> proyectos = new ArrayList<>();

	@OneToMany(mappedBy = "responsable", fetch = FetchType.EAGER)
	private List<TareaEntity> tareasAsignadas = new ArrayList<>();

	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
	private List<UsuarioProyectoEntity> proyectosParticipados = new ArrayList<>();

	@OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NotificacionUsuarioEntity> notificacionesRecibidas = new ArrayList<>();

}