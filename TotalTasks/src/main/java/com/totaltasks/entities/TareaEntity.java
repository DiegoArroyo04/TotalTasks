package com.totaltasks.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tarea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TareaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tarea", unique = true, nullable = false)
	private Long idTarea;

	@Column(name = "titulo", nullable = false)
	private String titulo;

	@Column(name = "descripcion")
	private String descripcion;

	// Insertable a False ya que la inseccion se hace desde la Base de datos
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", nullable = false, insertable = false)
	private Timestamp fechaCreacion;

	@Column(name = "fecha_limite")
	private Date fechaLimite;

	@Column(name = "estado", nullable = false)
	private String estado;

	@ManyToOne
	@JoinColumn(name = "id_proyecto", nullable = false)
	@JsonIgnore
	private ProyectoEntity proyecto;

	@ManyToOne
	@JoinColumn(name = "id_responsable", nullable = false)
	@JsonIgnore
	private UsuarioEntity responsable;

	@OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<NotificacionEntity> notificaciones = new ArrayList<>();

}