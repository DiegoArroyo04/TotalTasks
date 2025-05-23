const idProyecto = document.getElementById("idProyectoChat").value;
const idUsuario = document.getElementById("idUsuarioChat").value;
const userName = document.getElementById("nombreUsuario").value;
let conexion;

function websocketURL(ruta) {
	const protocolo = location.protocol === "https:" ? "wss:" : "ws:";
	return `${protocolo}//${location.host}${ruta}`;
}

window.addEventListener("load", () => {
	cargarMensajes();
	conectarChat();
});

function cargarMensajes() {
	$.getJSON(`/chat/mensajes/${idProyecto}`, (mensajes) => {
		const areaMensajes = $("#mensajesRecibidos").empty();
		mensajes.forEach((mensaje) => areaMensajes.append(crearHtmlMensaje(mensaje)));
		irAbajo();
	});
}

function conectarChat() {
	conexion = new WebSocket(websocketURL(`/chat/${idProyecto}`));

	conexion.onclose = () => setTimeout(conectarChat, 2000);

	conexion.onmessage = (evento) => {
		let mensaje;
		try {
			mensaje = JSON.parse(evento.data);
		} catch {
			return;
		}

		if (mensaje.type === "count") {
			$("#usuarios-conectados").text(mensaje.data);
			return;
		}

		$("#mensajesRecibidos").append(crearHtmlMensaje(mensaje));
		irAbajo();
	};
}

function enviarMensaje() {
	const textoMensaje = $("#mensajeInput").val().trim();
	if (!textoMensaje || conexion.readyState !== WebSocket.OPEN) return;

	const mensaje = {
		idUsuario: +idUsuario,
		contenido: textoMensaje,
		nombreUsuario: userName
	};

	conexion.send(JSON.stringify(mensaje));
	$("#mensajeInput").val("");
}

$("#mensajeInput").on("keydown", (event) => {
	if (event.key === "Enter") {
		enviarMensaje();
		event.preventDefault();
	}
});

function crearHtmlMensaje(mensaje) {
	const esMio = mensaje.idUsuario === +idUsuario;
	const nombreAutor = esMio ? userName : (mensaje.nombreUsuario || `Usuario ${mensaje.idUsuario}`);
	const fechaHora = mensaje.fechaCreacion ? new Date(mensaje.fechaCreacion) : new Date();
	const textoHora = formatearFechaCompleta(fechaHora);

	return `
		<div class="mensaje ${esMio ? "mensaje-mio" : "mensaje-otro"}">
			<div class="mensaje-burbuja">
				<div class="mensaje-header">
					<span class="autor">${nombreAutor}</span>
					<span class="hora">${textoHora}</span>
				</div>
				<div class="mensaje-texto">${mensaje.contenido}</div>
			</div>
		</div>
	`;
}

function irAbajo() {
	const contenedor = $("#mensajesRecibidos");
	contenedor.stop().animate({ scrollTop: contenedor[0].scrollHeight }, 300);
}

function formatearFechaCompleta(fecha) {
	const dosDigitos = (n) => n.toString().padStart(2, "0");
	const año = fecha.getFullYear();
	const mes = dosDigitos(fecha.getMonth() + 1);
	const dia = dosDigitos(fecha.getDate());
	const hora = dosDigitos(fecha.getHours());
	const minuto = dosDigitos(fecha.getMinutes());
	return `${dia}-${mes}-${año} &nbsp; &nbsp; ${hora}:${minuto}`;
}