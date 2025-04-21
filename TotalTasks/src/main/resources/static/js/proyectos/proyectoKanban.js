document.addEventListener("DOMContentLoaded", () => {

    const tableros = document.getElementById("tableros");
    const btnAgregar = document.getElementById("agregarColumna");
    const modal = document.getElementById("modalColumna");
    const inputNombre = document.getElementById("nombreColumna");
    const btnCrear = document.getElementById("crearColumna");
    const btnCancelar = document.getElementById("cancelarModal");

    // Mostrar el modal
    btnAgregar.addEventListener("click", () => {
        modal.style.display = "flex";
        inputNombre.value = "";
        inputNombre.focus();
    });

    // Cancelar modal
    btnCancelar.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Crear nueva columna
    btnCrear.addEventListener("click", () => {
        const nombre = inputNombre.value.trim();
        if (nombre) {
            const nuevaColumna = document.createElement("div");
            nuevaColumna.classList.add("columna");
            nuevaColumna.innerHTML = `
                <h3>${nombre} <button class="eliminar-columna">🗑️</button></h3>
                <div class="tareas" data-etapa="${nombre.toLowerCase().replace(/\s+/g, '')}">
                    <div class="tarea">🆕 Nueva tarea de ejemplo</div>
                </div>
                <button class="agregar-tarea">+ Añadir tarea</button>
            `;

            // Insertar la columna antes de "Hechas", si existe
            const columnas = tableros.querySelectorAll(".columna");
            const hecha = Array.from(columnas).find(col => col.querySelector("h3").textContent.toLowerCase().includes("hechas"));
            if (hecha) {
                tableros.insertBefore(nuevaColumna, hecha);
            } else {
                tableros.appendChild(nuevaColumna);
            }

            initSortable(); // activar drag en la nueva columna
            modal.style.display = "none";
        }
    });

    // Agregar tareas
    tableros.addEventListener("click", (p) => {
        if (p.target && p.target.classList.contains("agregar-tarea")) {
            const columna = p.target.closest(".columna");
            const tareas = columna.querySelector(".tareas");
            const nuevaTarea = document.createElement("div");
            nuevaTarea.classList.add("tarea");
            nuevaTarea.textContent = "🆕 Nueva tarea";
            tareas.appendChild(nuevaTarea);
        }
    });

    // Eliminar columna
    tableros.addEventListener("click", (e) => {
        if (e.target && e.target.classList.contains("eliminar-columna")) {
            const columna = e.target.closest(".columna");
            columna.remove();
        }
    });

    // Eliminar tarea
    tableros.addEventListener("click", (e) => {
        if (e.target && e.target.classList.contains("tarea")) {
            e.target.remove();
        }
    });

    // Inicializar SortableJS para tareas
    function initSortable() {
        const listas = document.querySelectorAll(".tareas");
        listas.forEach(lista => {
            new Sortable(lista, {
                group: "kanban",
                animation: 200,
                ghostClass: 'ghost'
            });
        });
    }

    initSortable(); // al cargar la página
});