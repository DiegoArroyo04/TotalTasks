const idProyecto = document.getElementById("idProyectoChat").value;
const idUsuario = document.getElementById("idUsuarioChat").value;
let conexion;

// Construye URL
function wsUrl(path) {
	const proto = window.location.protocol === "https:" ? "wss:" : "ws:";
	return `${proto}//${window.location.host}${path}`;
}

window.addEventListener("load", () => {
	cargarMensajes();
	conectarChat();
});

function cargarMensajes() {
	$.getJSON(`/chat/mensajes/${idProyecto}`, (data) => {
		const cont = $("#mensajesRecibidos").empty();
		data.forEach((msg) => cont.append(mensajeHtml(msg)));
		cont.scrollTop(cont.prop("scrollHeight"));
	});
}

function conectarChat() {
	conexion = new WebSocket(wsUrl(`/chat/${idProyecto}`));
	conexion.onopen = () => actualizarEstado("Conectado ✅");
	conexion.onerror = () => actualizarEstado("Error ❌");
	conexion.onclose = () => {
		actualizarEstado("Desconectado ❌");
		setTimeout(conectarChat, 2000);
	};

	conexion.onmessage = (evt) => {
		let msg;
		try {
			msg = JSON.parse(evt.data);
		} catch {
			return;
		}
		// mostramos cualquier mensaje (que viene guardado y reenviado)
		$("#mensajesRecibidos")
			.append(mensajeHtml(msg))
			.prop("scrollTop", $("#mensajesRecibidos").prop("scrollHeight"));
	};
}

function enviarMensaje() {
	const texto = $("#mensajeInput").val().trim();
	if (!texto || conexion.readyState !== WebSocket.OPEN) return;

	const payload = {
		idUsuario: +idUsuario,
		contenido: texto,
	};
	conexion.send(JSON.stringify(payload));
	$("#mensajeInput").val("");
}

$("#mensajeInput").keypress((e) => {
	if (e.which === 13) {
		enviarMensaje();
		return false;
	}
});

function actualizarEstado(texto) {
	$("#estado-conexion").text(texto);
}

function mensajeHtml(msg) {
	const quien =
		msg.idUsuario === +idUsuario ? "Yo" : `Usuario ${msg.idUsuario}`;
	return `
    <div class="mensaje">
      <strong>${quien}</strong> <span class="hora">${new Date(
		msg.fechaCreacion
	).toLocaleTimeString()}</span>
      <div class="contenido">${msg.contenido}</div>
    </div>
  `;
}