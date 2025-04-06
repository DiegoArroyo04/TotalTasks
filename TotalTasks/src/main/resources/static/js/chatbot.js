document.addEventListener("DOMContentLoaded", function () {

    //TIEMPO DE CARGA DEL BOTON
    setTimeout(() => {
        document.getElementById("chatToggle").classList.add("visible");
    }, 400);




    document.getElementById("chatToggle").addEventListener("click", function () {
        const chatbot = document.getElementById("chatWindow");

        if (chatbot.style.display === "none" || chatbot.style.display === "") {
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
        "crear tarea recurrente": "Para crear una tarea recurrente, selecciona la opción 'Repetir' al momento de agregar una nueva tarea.",
        "sincronizar con calendario": "Actualmente, ofrecemos inicio de sesión con Google, lo que facilitará la sincronización con Google Calendar próximamente",
        "seguridad de los datos": "Utilizamos protocolos de seguridad y cifrado de última generación para proteger tu información en todo momento.",

    };
    const respuesta = respuestas[opcion]


    // Mostrar la opción elegida
    chatLog.innerHTML += `<div class="chat user">👤 ${opcion}</div>`;

    // Mostrar respuesta del bot
    chatLog.innerHTML += `<div class="chat bot">🤖 ${respuesta}</div>`;

    // Hacer scroll automático
    chatLog.scrollTop = chatLog.scrollHeight;
}
