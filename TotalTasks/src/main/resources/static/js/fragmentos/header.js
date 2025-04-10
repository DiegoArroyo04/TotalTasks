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

    // Comprobar si el input de la sesión está presente en el DOM y su valor
    const sesionUsuario = document.getElementById('sesionUsuario') ? document.getElementById('sesionUsuario').value : null;
    console.log('ID del usuario:', sesionUsuario);  // Verificar el valor

    if (sesionUsuario && sesionUsuario !== "") {
        console.log('La sesión es válida, ID:', sesionUsuario);
    } else {
        console.log('No hay sesión activa');
    }

    // Cerrar sesión
    const cerrarSesionBtn = document.getElementById('cerrarSesion');
    if (cerrarSesionBtn) {
        cerrarSesionBtn.addEventListener('click', () => {
            if (sesionUsuario != null && sesionUsuario !== "") {
                const datos = {
                    usuarioId: sesionUsuario,
                    fechaHora: new Date().toISOString()
                };
                $.ajax({
                    url: '/usuarios/cerrarSesion',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(datos),
                    success: (response) => {
                        window.location.href = '/';
                    },
                    error: (error) => {
                        console.log('Error al cerrar sesión:', error);
                    }
                });
            } else {
                console.log('No hay sesión activa para cerrar.');
            }
        });
    }
});