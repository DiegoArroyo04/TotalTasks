// Espera a que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
    // Toggle Menú Perfil
    const perfilIcon = document.getElementById("perfilIcon");
    const menuPerfil = document.getElementById("menuPerfil");
    if (perfilIcon && menuPerfil) {
      perfilIcon.addEventListener("click", function() {
        menuPerfil.style.display = 
          menuPerfil.style.display === "block" ? "none" : "block";
      });
    }
  
    // Configurar botones para abrir el Modal de Creación
    const botonesModal = document.querySelectorAll(".btn-abrir-modal");
    botonesModal.forEach(function(boton) {
      boton.addEventListener("click", function() {
        // Leer data-attributes en lugar de pasar parámetros por onclick
        const repoName = boton.getAttribute("data-nombre") || "";
        const repoDescription = boton.getAttribute("data-descripcion") || "";
        document.getElementById("repoName").value = repoName;
        document.getElementById("repoDescription").value = repoDescription;
        document.getElementById("modalCrearProyecto").style.display = "block";
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