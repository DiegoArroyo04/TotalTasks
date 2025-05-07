document.addEventListener("DOMContentLoaded", () => {
	// Menú perfil
	const avatarMenu = document.getElementById("avatarMenu");
	const menuPerfil = document.getElementById("menuPerfil");

	if (avatarMenu && menuPerfil) {
		avatarMenu.addEventListener("click", () => {
			menuPerfil.style.display = menuPerfil.style.display === "block" ? "none" : "block";
		});

		// Ocultar al hacer clic fuera
		document.addEventListener("click", (e) => {
			if (!avatarMenu.contains(e.target) && !menuPerfil.contains(e.target)) {
				menuPerfil.style.display = "none";
			}
		});
	}

	// Menú notificaciones
	const notificacionesModal = document.getElementById("notificacionesModal");
	const botonNotificaciones = document.getElementById("notificaciones");

	if (notificacionesModal && botonNotificaciones) {
		botonNotificaciones.addEventListener("click", () => {
			notificacionesModal.style.display = notificacionesModal.style.display === "block" ? "none" : "block";
		});

		// Ocultar al hacer clic fuera
		document.addEventListener("click", (e) => {
			if (!botonNotificaciones.contains(e.target) && !notificacionesModal.contains(e.target)) {
				notificacionesModal.style.display = "none";
			}
		});
	}

});

function irAProyecto(idProyecto) {
	window.location.href = "/proyecto/" + idProyecto;
}