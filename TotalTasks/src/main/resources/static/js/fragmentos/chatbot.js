document.addEventListener("DOMContentLoaded", function () {
	// Tiempo de carga del boton
	setTimeout(() => {
		document.getElementById("chatToggle").classList.add("visible");
	}, 400);

	document
		.getElementById("chatToggle")
		.addEventListener("click", function () {
			const chatbot = document.getElementById("chatWindow");

			if (
				chatbot.style.display === "none" ||
				chatbot.style.display === ""
			) {
				openChatbot();
			} else {
				chatbot.style.display = "none";
				document.getElementById("moreQuestions").style.display = "none";
			}
		});
});

function openChatbot() {
	document.getElementById("chatWindow").style.display = "flex";
	document.getElementById("moreQuestions").style.display = "block";
}

function handleOptionClick(opcion) {
	const chatLog = document.getElementById("chatLog");
	const respuestas = {
		"unirme a un proyecto":
			"Ve al menú principal, haz clic en 'Unirme a proyecto' e ingresa el código que te compartieron.",
		"crear un sprint":
			"Desde la vista de proyecto, haz clic en 'Crear sprint', asigna fechas y selecciona tareas del backlog.",
		"personalizar un proyecto":
			"Puedes cambiar colores pulsando la opción personalizar tablero",
		"problemas con el login":
			"Asegúrate de estar usando el proveedor correcto (Google o GitHub) o tu usuario y contraseña y que tu cuenta esté autorizada.",
	};
	const respuesta = respuestas[opcion];

	// Mostrar la opción elegida
	chatLog.innerHTML += `<div class="chat user">👤 ${opcion}</div>`;

	// Mostrar respuesta del bot
	chatLog.innerHTML += `<div class="chat bot">🤖 ${respuesta}</div>`;

	// Hacer scroll automático
	chatLog.scrollTop = chatLog.scrollHeight;
}