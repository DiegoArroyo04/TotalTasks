document.getElementById("registro").addEventListener("submit", function () {
	event.preventDefault();
	// Si el formulario es valido se envia
	if (comprobarFormulario() == true) {
		var datos = {
			nombre: document.getElementById("nombre").value,
			usuario: document.getElementById("usuario").value,
			email: document.getElementById("email").value,
			contrasenia: document.getElementById("contrasenia").value,
		};

		// Realizar una solicitud AJAX PARA GUARDAR EL USUARIO EN BBDD
		$.ajax({
			type: "POST",
			url: "/usuarios/registrar",
			contentType: "application/json",
			data: JSON.stringify(datos),
			success: function (response) {
				if (response.trim() == "Cuenta creada con Éxito.") {
					mostrarModalSuccess();

					//REDIRIGIR AL LOGIN
					setTimeout(function () {
						window.location.href = "/login";
					}, 3000);
				} else {
					mostrarError(response);
				}
			},
		});
	}
});

function comprobarFormulario() {
	if (comprobarNombre() == true && comprobarContrasenia() == true) {
		return true;
	} else {
		return false;
	}
}

function comprobarNombre() {
	var nombre = document.getElementById("nombre").value;
	var validado = true;
	var numeros = "1234567890";

	// Si el nombre contiene numeros no es valido
	for (i = 0; i < nombre.length; i++) {
		if (numeros.includes(nombre[i])) {
			validado = false;
			mostrarError("Un nombre no puede contener números.");
		}
	}

	return validado;
}
function comprobarContrasenia() {
	var contrasenia = document.getElementById("contrasenia").value;

	// SI es menor de 8
	if (contrasenia.length < 8) {
		mostrarError("La contraseña debe tener minimo 8 caracteres.");
		return false;
	}

	// Si no tiene mayuscula
	if (!/[A-Z]/.test(contrasenia)) {
		mostrarError("La contraseña debe tener al menos una letra mayúscula.");
		return false;
	}

	// Si no tiene numero
	if (!/[0-9]/.test(contrasenia)) {
		mostrarError("La contraseña debe tener al menos un número");
		return false;
	}

	return true;
}

function abrirModalTerminos() {
	document.getElementById("modalTerminos").style.display = "flex";
}

// Cerrar modales
function cerrarModalTerminos() {
	document.getElementById("modalTerminos").style.display = "none";
}

function cerrarModalError() {
	document.getElementById("modalError").style.display = "none";
}

// Cerrar modales al hacer click fuera
window.addEventListener("click", function (event) {
	var modalTerminos = document.getElementById("modalTerminos");
	var modalError = document.getElementById("modalError");
	var modalSucess = document.getElementById("modalSuccess");

	if (event.target === modalTerminos) {
		cerrarModalTerminos();
	}

	if (event.target === modalError) {
		cerrarModalError();
	}

	if (event.target === modalSucess) {
		cerrarModalSuccess();
	}
});

function mostrarError(mensaje) {
	document.getElementById("modalError").style.display = "flex";
	document.getElementById("mensajeError").innerHTML = mensaje;
	setTimeout(function () {
		document.getElementById("modalError").style.display = "none";
	}, 3000);
}

function mostrarModalSuccess() {
	document.getElementById("modalSuccess").style.display = "flex";
}

function cerrarModalSuccess() {
	document.getElementById("modalSuccess").style.display = "none";
}