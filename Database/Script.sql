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
	color_primario VARCHAR(7) NOT NULL DEFAULT('#007BFF'),
    color_hover VARCHAR(7) NOT NULL DEFAULT('#0056b3'),
    custom_color VARCHAR(7) NULL,
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

-- Creación de la tabla Tablon
CREATE TABLE tablon (
    id_tablon BIGINT AUTO_INCREMENT,
    nombre_tablon VARCHAR(100) NOT NULL,
    orden INT NOT NULL,
    id_proyecto BIGINT,
    CONSTRAINT pk_tablon PRIMARY KEY (id_tablon),
    CONSTRAINT fk_tablon_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto)
);

-- Notificaciones
CREATE TABLE notificacion (
    id_notificacion BIGINT AUTO_INCREMENT,
    id_usuario_emisor BIGINT NULL,
    id_proyecto BIGINT NOT NULL,
    id_tarea BIGINT NULL,
    tipo VARCHAR(50) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_programada TIMESTAMP NULL,
    CONSTRAINT pk_notificacion PRIMARY KEY (id_notificacion),
    CONSTRAINT fk_notif_emisor FOREIGN KEY (id_usuario_emisor) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_notif_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),
    CONSTRAINT fk_notif_tarea FOREIGN KEY (id_tarea) REFERENCES tarea(id_tarea)
);

CREATE TABLE notificacion_usuario (
    id_notificacion_usuario BIGINT AUTO_INCREMENT,
    id_notificacion BIGINT NOT NULL,
    id_usuario_destinatario BIGINT NOT NULL,
    vista BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_notif_usuario PRIMARY KEY (id_notificacion_usuario),
    CONSTRAINT fk_notif_usuario_notif FOREIGN KEY (id_notificacion) REFERENCES notificacion(id_notificacion),
    CONSTRAINT fk_notif_usuario_dest FOREIGN KEY (id_usuario_destinatario) REFERENCES usuario(id_usuario)
);

CREATE TABLE product_backlog (
    id_backlog BIGINT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    story_points INT CHECK (story_points >= 0),
    prioridad INT DEFAULT 1,
    estado VARCHAR(50) DEFAULT 'pendiente',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_proyecto BIGINT NOT NULL,
    id_creador BIGINT NOT NULL,
    CONSTRAINT pk_product_backlog PRIMARY KEY (id_backlog),
    CONSTRAINT fk_backlog_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),
    CONSTRAINT fk_backlog_usuario FOREIGN KEY (id_creador) REFERENCES usuario(id_usuario)
);

-- Creación de la tabla Sprint
CREATE TABLE sprint (
    id_sprint BIGINT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_proyecto BIGINT NOT NULL,
    id_responsable BIGINT NOT NULL,
    estado VARCHAR(50) DEFAULT 'pendiente', -- Por ejemplo: 'pendiente', 'en_progreso', 'finalizado'
    CONSTRAINT pk_sprint PRIMARY KEY (id_sprint),
    CONSTRAINT fk_sprint_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),
    CONSTRAINT fk_sprint_responsable FOREIGN KEY (id_responsable) REFERENCES usuario(id_usuario)
);

CREATE TABLE product_board (
    id_tarea_board BIGINT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50) DEFAULT 'por_hacer', -- Estados: por_hacer, en_curso, hecho
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_proyecto BIGINT NOT NULL,
    id_responsable BIGINT NOT NULL,
    CONSTRAINT pk_product_board PRIMARY KEY (id_tarea_board),
    CONSTRAINT fk_board_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),
    CONSTRAINT fk_board_usuario FOREIGN KEY (id_responsable) REFERENCES usuario(id_usuario)
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

-- Tablones para Proyecto
INSERT INTO tablon (nombre_tablon, orden, id_proyecto) VALUES
('Pendiente', 1, 1),
('En progreso', 2, 1),
('Completado', 3, 1),
('Pendiente', 1, 2),
('En progreso', 2, 2),
('Completado', 3, 2);


-- Selects
SELECT * FROM USUARIO;
SELECT * FROM PROYECTO;
SELECT * FROM USUARIO_PROYECTO;
SELECT * FROM TAREA;
SELECT * FROM TABLON;
SELECT * FROM PRODUCT_BACKLOG;
SELECT * FROM SPRINT;
SELECT * FROM PRODUCT_BOARD;


-- Sript destructivo
DROP DATABASE totaltasks;