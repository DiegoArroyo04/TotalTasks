package com.totaltasks.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_proyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioProyectoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario_proyecto", unique = true, nullable = false)
	private Long idUsuarioProyecto;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioEntity usuario;

	@ManyToOne
	@JoinColumn(name = "id_proyecto", nullable = false)
	private ProyectoEntity proyecto;

	@Column(name = "rol")
	private String rol;

	@Column(name = "color_primario", length = 7, nullable = false)
	private String colorPrimario = "#007BFF";

	@Column(name = "color_hover", length = 7, nullable = false)
	private String colorHover = "#0056b3";

	@Column(name = "custom_color", length = 7, nullable = true)
	private String customColor;

	@Override
	public String toString() {
		return "UsuarioProyectoEntity [proyecto=" + proyecto + "]";
	}

}