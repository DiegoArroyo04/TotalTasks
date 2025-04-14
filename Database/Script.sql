-- Creación base de datos en MySQL
CREATE DATABASE totaltasks;
USE totaltasks;

-- Creación de la tabla Usuario
CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasenia VARCHAR(255) NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foto_perfil LONGBLOB NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario)
);

-- Creación de la tabla Proyecto
CREATE TABLE proyecto (
    id_proyecto BIGINT AUTO_INCREMENT,
    nombre_proyecto VARCHAR(100) NOT NULL,
    descripcion TEXT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metodologia VARCHAR(100) NOT NULL,
    id_creador BIGINT,
    codigo VARCHAR(10) UNIQUE,
    CONSTRAINT pk_proyecto PRIMARY KEY (id_proyecto),
    CONSTRAINT fk_proyecto_usuario FOREIGN KEY (id_creador) REFERENCES Usuario (id_usuario) 
);

-- Creación de la tabla intermedia Usuario_Proyecto
CREATE TABLE usuario_proyecto (
    id_usuario_proyecto BIGINT AUTO_INCREMENT,
    id_usuario BIGINT,
    id_proyecto BIGINT,
    rol VARCHAR(100),
    CONSTRAINT pk_usuario_proyecto PRIMARY KEY (id_usuario_proyecto),
    CONSTRAINT fk_usuario_proyecto_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario) ,
    CONSTRAINT fk_usuario_proyecto_proyecto FOREIGN KEY (id_proyecto) REFERENCES Proyecto (id_proyecto) 
);

-- Creación de la tabla Tarea
CREATE TABLE tarea (
    id_tarea BIGINT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_limite DATE NULL,
    id_proyecto BIGINT,
    id_responsable BIGINT,
    estado VARCHAR(100),
    CONSTRAINT pk_tarea PRIMARY KEY (id_tarea),
    CONSTRAINT FOREIGN KEY (id_proyecto) REFERENCES Proyecto (id_proyecto),
    CONSTRAINT FOREIGN KEY (id_responsable) REFERENCES Usuario (id_usuario)
);

SELECT * FROM USUARIO;



INSERT INTO proyecto (nombre_proyecto, descripcion, metodologia, id_creador)
VALUES ('Proyecto Demo', 'Este es un proyecto de prueba', 'SCRUM', 3);


-- Juan participa como desarrollador en el segundo proyecto
INSERT INTO usuario_proyecto (id_usuario, id_proyecto, rol)
VALUES (3, 2, 'Developer');



-- SCRIPT DE DESTRUCTIVO
DROP DATABASE totaltasks;