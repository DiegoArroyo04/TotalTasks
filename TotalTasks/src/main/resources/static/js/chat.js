let conexion;
let idProyecto = document.getElementById("idProyectoChat").value;

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
  const usuario = document.getElementById("nombreUsuario").value;
  if (texto && conexion.readyState === WebSocket.OPEN) {
    const mensaje = {
      usuario: usuario,
      contenido: texto,
      hora: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) // HH:MM
    };
    conexion.send(JSON.stringify(mensaje));
    entrada.value = "";
  }
}


function mostrarMensaje(data) {
  const contenedorMensajes = document.getElementById("mensajesRecibidos");
  const chatBody = document.querySelector(".chat-body");

  let mensaje;
  try {
    mensaje = JSON.parse(data);
  } catch (e) {
    // Si no es JSON válido, mostrar como texto plano
    mensaje = { contenido: data, usuario: "Desconocido", hora: "--:--" };
  }


  if (mensaje.type === "count") {
    document.getElementById("estado-conexion").textContent = `Usuarios conectados: ${mensaje.data}`;
    return;
  }

  const msgDiv = document.createElement("div");
  msgDiv.classList.add("mensaje");

  msgDiv.innerHTML = `
    <div class="mensaje-usuario">
      <strong>${mensaje.usuario}</strong> <span class="mensaje-hora">${mensaje.hora}</span>
    </div>
    <div class="mensaje-contenido">${mensaje.contenido}</div>
  `;


  contenedorMensajes.appendChild(msgDiv);
  chatBody.scrollTop = chatBody.scrollHeight;



}

function actualizarEstado(estado) {
  document.getElementById("estado-conexion").textContent = estado;
}
