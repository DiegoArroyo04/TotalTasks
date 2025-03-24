// Caracterteristicas

document.addEventListener("DOMContentLoaded", () => {
  const botonesPestana = document.querySelectorAll(".boton-pestana");
  const contenidosPestana = document.querySelectorAll(".contenido-pestana");

  botonesPestana.forEach(boton => {
    boton.addEventListener("click", () => {

      // Elimina la clase "active" de todos los botones y contenidos
      botonesPestana.forEach(b => b.classList.remove("active"));
      contenidosPestana.forEach(c => c.classList.remove("active"));

      // Agrega la clase "active" al botón clicado y muestra su contenido
      boton.classList.add("active");
      const idPestana = boton.getAttribute("data-pestana");
      document.getElementById(idPestana).classList.add("active");
      
    });
  });
});