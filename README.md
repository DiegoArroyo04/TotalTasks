<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/nuevoLogo_SinFondo.png?raw=true" alt="Logo TotalTasks" width="250">
</p>

<h1 align="center">TotalTasks 🧩️ – Plataforma de Gestión de Tareas</h1>

<p align="center">
  <img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build"/>
  <img src="https://img.shields.io/badge/license-MIT-blue" alt="License"/>
  <img src="https://img.shields.io/badge/status-en%20desarrollo-yellow" alt="Status"/>
  <img src="https://img.shields.io/badge/Java-17+-red" alt="Java"/>
  <img src="https://img.shields.io/badge/MySQL-8.x-blue" alt="MySQL"/>
</p>

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

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/landingPage.png?raw=true" alt="Landing page" width="900">
</p>

* Accesos directos a Inicio, Características, FAQ.
* Imagen principal con CTA llamativa.
* Secciones para diferentes perfiles: ventas, RRHH, marketing, etc.

### 🔑 Inicio de Sesión

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/loginPage.png?raw=true" alt="Login" width="900">
</p>

* Login manual: usuario y contraseña.
* Login OAuth con Google y GitHub:

  * Google: usa Google Identity + JWT.
  * GitHub: flujo OAuth + permisos de perfil/repos.

> ⚠️ **Todas las contraseñas se encriptan con Bcrypt.**

### 📅 Registro de Usuarios

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/RegisterPage.png?raw=true" alt="Register" width="900">
</p>

* Formulario validado con:

  * Nombre (sin números)
  * Usuario
  * Email
  * Contraseña fuerte (8+ caracteres, mayúsculas y números)
* Modales suaves para feedback visual.

### 📊 Dashboard

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/Dashboard.png?raw=true" alt="Dashboard" width="900">
</p>

* Crear proyectos.
* Personalizar tu perfil.
* Ver tus proyectos de Github.

### 📊 Proyecto kanban

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/Kanban.png?raw=true" alt="Kanban" width="900">
</p>

* Crear columnas y tareas al vuelo.
* Mover tareas con drag & drop.
* Estadísticas por proyecto (commits, lenguajes, PRs).
* Integrado con Chart.js + FullCalendar.

### 📊 Proyecto Scrum

<p align="center">
  <img src="https://github.com/DiegoArroyo04/TotalTasks/blob/main/ContenidoREADME/Scrum.png?raw=true" alt="Scrum" width="900">
</p>

* Crear historias de usuario.
* Hacer sprints personalizados.
* Tablón de tareas
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

![Java](https://img.shields.io/badge/Java-17+-red)
![Maven](https://img.shields.io/badge/Maven-3.6+-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![VSCode](https://img.shields.io/badge/Editor-VSCode-brightgreen)

* Editor de código (Recomendado: VSCode)
* Java 17+
* Maven 3.6+
* Gestor de base de datos (MySQL Workbench u otro)
* MySQL 8.x
* Cuenta Google + GitHub para pruebas OAuth (opcional)

---

# ⚙️ Instalación y Configuración

## 1. Descargar proyecto

Opcion 1 -> Hacer click en el botón de `<> Code` y darle a la opción de `Download ZIP`

Opcion 2 -> Abrir terminal de Git Bash en la ubicación deseada y ejecutar:

```bash
git clone https://github.com/DiegoArroyo04/TotalTasks.git
```

## 2. Crear la base de datos

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

## 3. Ejecutar el proyecto

Abrir el navegador en la ruta:

```propierties
http://localhost:9091
```

---

# 📂 Estructura del Proyecto

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

# 👨‍💼 Autores

**TotalTasks** ha sido desarrollado como parte de un proyecto de grado con una proyección profesional.

* **DiegoArroyo04** – [diegoarroyogonzalez04@gmail.com](mailto:diegoarroyogonzalez04@gmail.com)
* **JrubioCode** – [javii.central.7@hotmail.com](mailto:javii.central.7@hotmail.com)

---

© 2025 TotalTasks. Proyecto de software libre con fines académicos y profesionales.
