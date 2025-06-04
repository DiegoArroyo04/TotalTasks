# TotalTasks 🧩️ – Plataforma de Gestión de Tareas Ágil y Colaborativa

**TotalTasks** es una plataforma web avanzada para la organización de proyectos y tareas usando metodologías ágiles como **Scrum** y **Kanban**. Fue desarrollada como parte de un proyecto académico con un enfoque profesional y escalable, pensada para facilitar el trabajo en equipo, mejorar la productividad y ofrecer una alternativa gratuita a herramientas privativas.

---

## Descripción General

TotalTasks nace con la idea de proporcionar una herramienta accesible y potente para estudiantes, pequeños equipos y startups que necesitan un espacio colaborativo para organizar sus proyectos.

* Interfaz limpia, responsiva y adaptativa.
* Integraciones inteligentes con GitHub y Google Calendar.
* Chat interno, notificaciones y perfil de usuario.
* Sistema seguro de autenticación con cifrado de contraseñas.

---

## 🚀 Características Principales

* 📋 Gestión de proyectos, tareas y sprints.
* 🏗️ Tablero Kanban con columnas y tareas dinámicas.
* ↺ Drag & Drop fluido, con sincronización backend.
* 🔐 Inicio de sesión manual, Google y GitHub.
* 📊 Estadísticas interactivas con Chart.js.
* 🡥 Colaboración multiusuario.
* 🗎️ Notificaciones en tiempo real.
* 🗓️ Integración con Google Calendar.
* 🌐 Multiplataforma y 100% responsive.

---

## 🗃️ Pantallas de la Aplicación

### 🎨 Landing Page

> *(Deja un hueco para la captura de pantalla)*

* Accesos directos a Inicio, Características, FAQ.
* Imagen principal con CTA llamativa.
* Secciones para diferentes perfiles: ventas, RRHH, marketing, etc.

### 🔑 Inicio de Sesión

> *(Captura recomendada del formulario con botones de Google y GitHub)*

* Login manual: usuario y contraseña.
* Login OAuth con Google y GitHub:

  * Google: usa Google Identity + JWT.
  * GitHub: flujo OAuth + permisos de perfil/repos.

> ⚠️ **Todas las contraseñas se encriptan con Bcrypt.**

### 📅 Registro de Usuarios

> *(Captura recomendada del formulario de registro)*

* Formulario validado con:

  * Nombre (sin números)
  * Usuario
  * Email
  * Contraseña fuerte (8+ caracteres, mayúsculas y números)
* Modales suaves para feedback visual.

### 📊 Dashboard y Kanban

> *(Captura recomendada del tablero Kanban y dashboard con gráficos)*

* Crear columnas y tareas al vuelo.
* Mover tareas con drag & drop.
* Estadísticas por proyecto (commits, lenguajes, PRs).
* Integrado con Chart.js + FullCalendar.

---

## 🧠 Tecnologías Utilizadas

| Área          | Tecnología                |
| ------------- | ------------------------- |
| Backend       | Java 17, Spring Boot      |
| Seguridad     | Spring Security, Bcrypt   |
| Frontend      | HTML, CSS, JS, Thymeleaf  |
| Plantillas    | Thymeleaf                 |
| Base de Datos | MySQL + Hibernate         |
| Gráficos      | Chart.js                  |
| Calendario    | FullCalendar + Google API |
| Chat          | WebSocket                 |
| UI Dinámica   | jQuery, AJAX              |
| OAuth         | Google Identity, GitHub   |

---

## ✅ Requisitos Previos

* Editor de codigo+ (Recomendado VsCode)
* Java 17+
* Maven 3.6+
* Gestor de base de datos+
* MySQL 8.x
* Cuenta Google + GitHub para pruebas OAuth (opcional)

---

# ⚙️ Instalación y Configuración

## 1. Descargar proyecto

### Primer paso

Opcion 1 -> Hacer click en el botón de `<> Code` y darle a la opción de `Download ZIP`

Opcion 2 -> Abrir terminal de Git Bash en la ubicación deseada y ejecutar:

```bash
git clone https://github.com/DiegoArroyo04/TotalTasks.git
```

### Segundo paso

Abrir tu gestor de base de datos y ejecutar el archivo `Script.sql` ubicado en:

```text
TotalTasks/
├── Database/
│   ├── Modelo de datos.mwb
│   └── Script.sql
├── Documentation/
├── src/
│   ├── ...
└── README.md
```

### Tercer paso

Ejecutar el proyecto

---

## 📂 Estructura del Proyecto

```text
TotalTasks/
├── Database/
│   ├── Modelo de datos.mwb
│   └── Script.sql
├── Documentation/
├── src/
│   ├── main/java/com/totaltasks/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   ├── resources/
│   │   ├── static/
│   │   │   ├── css/
│   │   │   ├── js/
│   │   │   └── images/
│   │   ├── templates/
│   │   │   ├── fragments/
│   │   │   ├── login.html
│   │   │   ├── dashboard.html
│   │   │   └── ...
│   │   └── application.properties
└── README.md
```

---

## 👨‍💼 Autores

**TotalTasks** ha sido desarrollado como parte de un proyecto de grado con una proyección profesional.

* **DiegoArroyo04** – [diegoarroyogonzalez04@gmail.com](mailto:diegoarroyogonzalez04@gmail.com)
* **JrubioCode** – [javii.central.7@hotmail.com](mailto:javii.central.7@hotmail.com)

---

© 2025 TotalTasks. Proyecto de software libre con fines académicos y profesionales.
