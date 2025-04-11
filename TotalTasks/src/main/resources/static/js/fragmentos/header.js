// Asegúrate de que el DOM esté completamente cargado antes de ejecutar cualquier cosa
document.addEventListener('DOMContentLoaded', () => {
    // Función para manejar el scroll y fijar el header
    window.addEventListener("scroll", () => {
        const header = document.getElementById("cabezera");
        const spacer = document.getElementById("headerSpacer");

        if (header && spacer) {
            const headerHeight = header.offsetHeight;
            if (window.scrollY > 120) {
                header.classList.add("fixed");
                spacer.style.height = `${headerHeight}px`;
            } else {
                header.classList.remove("fixed");
                spacer.style.height = '0px';
            }
        }
    });

    const avatarMenu = document.getElementById('avatarMenu');
    const menuPerfil = document.getElementById('menuPerfil');

    if (avatarMenu && menuPerfil) {
        avatarMenu.addEventListener("click", () => {
            menuPerfil.style.display = menuPerfil.style.display === "block" ? "none" : "block";
        });
    }
});