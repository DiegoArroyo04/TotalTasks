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

-- Inserción de proyectos
INSERT INTO proyecto (nombre_proyecto, descripcion, metodologia, id_creador, codigo) VALUES 
('Proyecto Alpha', 'Sistema de gestión de tareas', 'Scrum', 1, 'ALPHA123'), 
('Proyecto Beta', 'App de colaboración en equipo', 'Kanban', 2, 'BETA456');

-- Inserción en tabla intermedia usuario_proyecto
INSERT INTO usuario_proyecto (id_usuario, id_proyecto, rol) VALUES
(1, 1, 'Administrador'),
(1, 2, 'Colaborador'),
(2, 2, 'Administrador');

-- Inserción de tareas
INSERT INTO tarea (titulo, descripcion, fecha_limite, id_proyecto, id_responsable, estado) VALUES
('Diseñar base de datos', 'Diseñar el esquema inicial de la base de datos', '2025-04-20', 1, 1, 'En progreso'),
('Maquetar la app', 'Hacer la estructura de pantallas principales', '2025-04-22', 2, 2, 'Pendiente');





-- Selects
SELECT * FROM USUARIO;
SELECT * FROM PROYECTO;
SELECT * FROM USUARIO_PROYECTO;
SELECT * FROM TAREA;


-- Sript destructivo
DROP DATABASE totaltasks;