// Funcionalidades
document.addEventListener("DOMContentLoaded", () => {
  const botonesPestana = document.querySelectorAll(".pestanas-funcionalidades .boton-pestana");
  const contenidosPestana = document.querySelectorAll(".pestanas-funcionalidades .contenido-pestana");

  botonesPestana.forEach(boton => {
    boton.addEventListener("click", () => {
      // Remueve la clase "active" de todos los botones y contenidos
      botonesPestana.forEach(b => b.classList.remove("active"));
      contenidosPestana.forEach(c => c.classList.remove("active"));

      // Activa el botón clicado y muestra su contenido
      boton.classList.add("active");
      const idPestana = boton.getAttribute("data-pestana");
      document.getElementById(idPestana).classList.add("active");
    });
  });
});