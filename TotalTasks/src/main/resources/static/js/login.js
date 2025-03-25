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

// CERRAR MODALES AL HACER CLICK FUERA 
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
        contrasenia: document.getElementById("contrasenia").value
    };

    // Realizar una solicitud AJAX PARA BUSCAR EL USUARIO EN BBDD
    $.ajax({
        type: "POST",
        url: "/usuarios/comprobarLogin",
        contentType: "application/json",
        data: JSON.stringify(datos),
        success: function (response) {

            if (response.trim() == "Encontrado") {

                //REDIRIGIR AL DASHBOARD
                window.location.href = '/dashboard';

            } else {
                mostrarError(response);
            }
        },
        error: function (error) {
            console.error("Error en la solicitud AJAX:", error);
        }
    });
});