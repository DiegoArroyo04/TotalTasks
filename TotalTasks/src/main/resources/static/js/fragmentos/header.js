document.addEventListener('DOMContentLoaded', () => {

    // Menú perfil
    const avatarMenu = document.getElementById('avatarMenu');
    const menuPerfil = document.getElementById('menuPerfil');

    if (avatarMenu && menuPerfil) {
        avatarMenu.addEventListener("click", () => {
            menuPerfil.style.display = (menuPerfil.style.display === "block") ? "none" : "block";
        });

        // Ocultar al hacer clic fuera
        document.addEventListener("click", (e) => {
            if (!avatarMenu.contains(e.target) && !menuPerfil.contains(e.target)) {
                menuPerfil.style.display = "none";
            }
        });
    }
    
});