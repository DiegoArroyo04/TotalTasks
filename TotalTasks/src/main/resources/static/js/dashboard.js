// Espera a que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
  // Toggle para Menú Perfil (opcional, solo si existen estos elementos en tu HTML)
  const perfilIcon = document.getElementById("perfilIcon");
  const menuPerfil = document.getElementById("menuPerfil");
  if (perfilIcon && menuPerfil) {
    perfilIcon.addEventListener("click", function() {
      menuPerfil.style.display = menuPerfil.style.display === "block" ? "none" : "block";
    });
  }
  
  // Configurar botones para abrir el Modal de Creación desde Repositorio
  // Se ha actualizado la selección para que use la nueva clase .btn-modal
  const botonesModal = document.querySelectorAll(".btn-modal");
  botonesModal.forEach(function(boton) {
    boton.addEventListener("click", function() {
      // Leer los atributos de datos (data-nombre y data-descripcion)
      const repoName = boton.getAttribute("data-nombre") || "";
      const repoDescription = boton.getAttribute("data-descripcion") || "";
      document.getElementById("repoName").value = repoName;
      document.getElementById("repoDescription").value = repoDescription;
      // Se puede usar "flex" si se desea centrar el modal con flexbox
      document.getElementById("modalCrearProyecto").style.display = "flex";
    });
  });
});

// Función para cerrar el modal
function cerrarModal() {
  document.getElementById("modalCrearProyecto").style.display = "none";
}

// Cerrar el modal si se hace click fuera de él
window.addEventListener("click", function(event) {
  const modal = document.getElementById("modalCrearProyecto");
  if (event.target === modal) {
    modal.style.display = "none";
  }
});