package com.totaltasks.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tablon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TablonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tablon")
	private Long id;

	@Column(name = "nombre_tablon", nullable = false)
	private String nombreTablon;

	@Column(name = "orden", nullable = false)
	private Integer orden;

	@ManyToOne
	@JoinColumn(name = "id_proyecto", nullable = false)
	private ProyectoEntity proyecto;

}