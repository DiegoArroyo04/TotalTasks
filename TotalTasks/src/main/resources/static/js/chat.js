let conexion;

function conectarChat() {
	conexion = new WebSocket("ws://" + window.location.host + "/chat");

	conexion.onopen = () => actualizarEstado("Conectado ✅");
	conexion.onclose = () => actualizarEstado("Desconectado ❌");
	conexion.onerror = () => actualizarEstado("Error ❌");

	conexion.onmessage = (evento) => {
		try {
			const mensaje = JSON.parse(evento.data);
			if (mensaje.type === "count") {
				mostrarMensaje("👥 Usuarios conectados: " + mensaje.data);
			} else {
				mostrarMensaje("💬 " + mensaje.data);
			}
		} catch {
			mostrarMensaje("💬 " + evento.data);
		}
	};
}

function enviarMensaje() {
	const entrada = document.getElementById("mensajeInput");
	const texto = entrada.value.trim();

	if (texto && conexion?.readyState === WebSocket.OPEN) {
		conexion.send(texto);
		entrada.value = "";
	}
}

function mostrarMensaje(texto) {
	const chat = document.getElementById("mensajesRecibidos");
	const mensaje = document.createElement("p");
	mensaje.textContent = texto;
	chat.appendChild(mensaje);
	chat.scrollTop = chat.scrollHeight;
}

function actualizarEstado(estado) {
	document.getElementById("estado-conexion").textContent = estado;
}

window.addEventListener("load", conectarChat);