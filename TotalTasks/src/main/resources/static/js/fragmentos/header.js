document.addEventListener('DOMContentLoaded', () => {
    const header = document.getElementById("cabezera");
    const spacer = document.getElementById("headerSpacer");

    window.addEventListener('scroll', function() {
        const header = document.getElementById('cabezera');
        const spacer = document.getElementById('headerSpacer');
        
        // Activar la clase .fixed cuando el scroll pase de 100px
        if (window.scrollY > 100) {
            header.classList.add('fixed');
            spacer.style.height = '200px'; // Esto asegura que el contenido no se oculte
        } else {
            header.classList.remove('fixed');
            spacer.style.height = '0'; // Vuelve a la altura original
        }
    });

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