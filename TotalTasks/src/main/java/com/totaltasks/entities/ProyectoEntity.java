package com.totaltasks.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

import java.util.List;

@Entity
@Table(name = "proyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto", unique = true, nullable = false)
    private Long idProyecto;

    @Column(name = "nombre_proyecto", nullable = false)
    private String nombreProyecto;

    @Column(name = "descripcion")
    private String descripcion;

    // INSERTABLE FALSE PORQUE LA INSERCCION DE LA FECHA SE HACE DESDE BBDD
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, insertable = false)
    private Timestamp fechaCreacion;

    @Column(name = "metodologia", nullable = false)
    private String metodologia;

    @ManyToOne
    @JoinColumn(name = "id_creador", nullable = false)
    private UsuarioEntity creador;

    @OneToMany(mappedBy = "proyecto")
    private List<TareaEntity> tareas;

    @OneToMany(mappedBy = "proyecto")
    private List<UsuarioProyectoEntity> usuarios;
}