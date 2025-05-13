let conexion;
let idProyecto = document.getElementById('idProyectoChat').value;

window.addEventListener("load", () => {
	conectarChat();
});

function conectarChat() {
	conexion = new WebSocket(`ws://${window.location.host}/chat/${idProyecto}`);
	conexion.onopen = () => actualizarEstado("Conectado ✅");
	conexion.onclose = () => actualizarEstado("Desconectado ❌");
	conexion.onerror = () => actualizarEstado("Error ❌");
	conexion.onmessage = (evt) => mostrarMensaje(evt.data);
}

document.addEventListener("keydown", function (event) {
	const entrada = document.getElementById("mensajeInput");
	if (event.key === "Enter" && document.activeElement === entrada) {
		enviarMensaje();
	}
});

function enviarMensaje() {
	const entrada = document.getElementById("mensajeInput");
	const texto = entrada.value.trim();
	if (texto && conexion.readyState === WebSocket.OPEN) {
		conexion.send(texto);
		entrada.value = "";
	}
}

function mostrarMensaje(texto) {
	const cont = document.getElementById("mensajesRecibidos");
	const p = document.createElement("p");
	p.textContent = texto;
	cont.appendChild(p);
	cont.scrollTop = cont.scrollHeight;
}

function actualizarEstado(estado) {
	document.getElementById("estado-conexion").textContent = estado;
}