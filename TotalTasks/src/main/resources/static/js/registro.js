document.getElementById("registro").addEventListener("submit", function () {
    event.preventDefault();
    comprobarFormulario();
});

function comprobarFormulario() {
    comprobarNombre();
    comprobarContrasenia();
}
function comprobarNombre() {

    var nombre = document.getElementById("nombre").value;
    var validado = true;
    var numeros = "1234567890";

    for (i = 0; i < nombre.length; i++) {
        if (numeros.includes(nombre[i])) {
            validado = false;
        }
    }

    return validado;
}
function comprobarContrasenia() {

}

function abrirModalTerminos() {
    document.getElementById("modalTerminos").style.display = "flex";
}


//CERRAR MODALES
function cerrarModalTerminos() {
    document.getElementById("modalTerminos").style.display = "none";
}

function cerrarModalError() {
    document.getElementById("modalError").style.display = "none";
}

// CERRAR MODALES AL HACER CLICK FUERA 
window.addEventListener("click", function (event) {
    var modalTerminos = document.getElementById("modalTerminos");
    var modalError = document.getElementById("modalError");
    if (event.target === modalTerminos) {
        cerrarModalTerminos();
    }

    if (event.target === modalError) {
        cerrarModalError();
    }
});

function mostrarError(mensaje) {
    document.getElementById("modalError").style.display = "flex";
    document.getElementById("mensajeError").innerHTML = mensaje;
}