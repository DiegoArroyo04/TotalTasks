function cerrarModalError() {
	document.getElementById("modalError").style.display = "none";
}

function mostrarError(mensaje) {
	document.getElementById("modalError").style.display = "flex";
	document.getElementById("mensajeError").innerHTML = mensaje;
	setTimeout(function () {
		document.getElementById("modalError").style.display = "none";
	}, 3000);
}

// Cerrar modales al hacer click fuera
window.addEventListener("click", function (event) {
	var modalError = document.getElementById("modalError");

	if (event.target === modalError) {
		cerrarModalError();
	}
});

document.getElementById("login").addEventListener("submit", function () {
	event.preventDefault();

	var datos = {
		usuario: document.getElementById("usuario").value,
		contrasenia: document.getElementById("contrasenia").value,
	};

	// Realizar una solicitud AJAX para buscar al usuario en la BBDD
	$.ajax({
		type: "POST",
		url: "/usuarios/comprobarLogin",
		contentType: "application/json",
		data: JSON.stringify(datos),
		success: function (response) {
			if (response.trim() == "Encontrado") {
				//REDIRIGIR AL DASHBOARD
				window.location.href = "/dashboard";
			} else {
				mostrarError(response);
			}
		},
	});
});

// Función para manejar la respuesta del inicio de sesión con Google
function handleCredentialResponse(response) {
	// Decodificar el token de Google
	const data = jwt_decode(response.credential);

	let email = data.email;
	let username = email.split("@")[0];

	// Crear el objeto con los datos necesarios para el registro
	const usuarioData = {
		nombre: (data.given_name + " " + (data.family_name || "")).trim(),
		usuario: username,
		email: email,
		fotoPerfilGoogle: data.picture,
	};

	// Enviar los datos al backend para registrar o iniciar sesión
	$.ajax({
		type: "POST",
		url: "/usuarios/registrarGoogle",
		contentType: "application/json",
		data: JSON.stringify(usuarioData),
		success: function (response) {
			if (
				response
					.trim()
					.includes("Usuario encontrado. Iniciando sesión...") ||
				response.trim().includes("Cuenta creada con Éxito.")
			) {
				// Si el usuario se registra o ya existe, redirigir al dashboard
				window.location.href = "/dashboard";
			}
		},
		error: function (error) {
			mostrarError("Error en la comunicación con el servidor.");
		},
	});
}

// Inicialización del botón de Google
function initGoogleSignIn() {
	google.accounts.id.initialize({
		client_id:
			"978494635156-kn9vmufnugkahlhptbvmtah7jt7r60h8.apps.googleusercontent.com",
		callback: handleCredentialResponse,
	});
	google.accounts.id.renderButton(document.querySelector(".g_id_signin"), {
		type: "icon",
		theme: "filled_black",
	});
	google.accounts.id.prompt(); // Muestra el prompt de Google
}

// Inicializar Google Sign-In al cargar la página
window.onload = function () {
	initGoogleSignIn();
};

// Login con GitHub con permisos extendidos
document.getElementById("github-login").addEventListener("click", function () {
	const clientId = "Ov23li9EsZ9MUsqhPpoX";
	const redirectUri = encodeURIComponent(
		"http://localhost:9091/usuarios/githubCallback"
	);
	const scope = encodeURIComponent(
		"read:user user:email repo admin:repo_hook delete_repo gist"
	);
	window.location.href = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`;
});