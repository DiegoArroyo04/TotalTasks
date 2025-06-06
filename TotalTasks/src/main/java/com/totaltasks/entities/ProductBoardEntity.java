package com.totaltasks.entities;

import java.sql.Timestamp;

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
@Table(name = "product_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTareaBoard;

	@Column(name="titulo")
    private String titulo;

	@Column(name="descripcion")
    private String descripcion;

	@Column(name = "story_points")
    private Integer storyPoints;

    @Column(name = "prioridad")
    private String prioridad;

	@Column(name="estado")
    private String estado = "por_hacer";

    @Column(name = "fecha_creacion")
    private Timestamp fechaCreacion;

	@Column(name = "fecha_limite")
    private Timestamp fechaLimite;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private ProyectoEntity proyecto;

    @ManyToOne
    @JoinColumn(name = "id_responsable", nullable = false)
    private UsuarioEntity responsable;

}