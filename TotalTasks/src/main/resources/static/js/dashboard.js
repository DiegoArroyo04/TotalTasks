// Espera a que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
  // Toggle menú perfil (opcional)
  const perfilIcon = document.getElementById("perfilIcon");
  const menuPerfil = document.getElementById("menuPerfil");
  if (perfilIcon && menuPerfil) {
    perfilIcon.addEventListener("click", function() {
      menuPerfil.style.display = menuPerfil.style.display === "block" ? "none" : "block";
    });
  }

  // Abrir modal desde repo
  const botonesModal = document.querySelectorAll(".btn-modal");
  botonesModal.forEach(function(boton) {
    boton.addEventListener("click", function() {
      const repoName = boton.getAttribute("data-nombre") || "";
      const repoDescription = boton.getAttribute("data-descripcion") || "";
      document.getElementById("repoName").value = repoName;
      document.getElementById("repoDescription").value = repoDescription;
      document.getElementById("modalCrearProyecto").style.display = "flex";
    });
  });

  // Mostrar modal de error si existe parámetro en la URL
  const urlParams = new URLSearchParams(window.location.search);
  const error = urlParams.get("error");
  if (error) {
    const modalError = document.getElementById("modalError");
    const mensajeElem = document.getElementById("mensajeError");
    if (modalError && mensajeElem) {
      mensajeElem.textContent = decodeURIComponent(error);
      modalError.style.display = "flex";
    }
  }
});

// Funciones de los Modales

function cerrarModal() {
  document.getElementById("modalCrearProyecto").style.display = "none";
}

function abrirModalCrearProyectoManual() {
  document.getElementById("modalCrearProyectoManual").style.display = "flex";
}
function cerrarModalCrearProyectoManual() {
  document.getElementById("modalCrearProyectoManual").style.display = "none";
}

function abrirModalUnirse() {
  document.getElementById("modalUnirse").style.display = "flex";
}

function cerrarModalUnirse() {
  document.getElementById("modalUnirse").style.display = "none";
}

function cerrarModalError() {
  document.getElementById("modalError").style.display = "none";
}

// Cerrar cualquier modal haciendo clic fuera
window.addEventListener("click", function(event) {
  const modalError = document.getElementById("modalError");

  if (event.target === modalError) {
    cerrarModalError();
  }
});