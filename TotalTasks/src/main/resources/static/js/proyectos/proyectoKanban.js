document.addEventListener("DOMContentLoaded", () => {

    const tableros = document.getElementById("tableros");
    const btnAgregar = document.getElementById("agregarColumna");
    const modal = document.getElementById("modalColumna");
    const inputNombre = document.getElementById("nombreColumna");
    const btnCrear = document.getElementById("crearColumna");
    const btnCancelar = document.getElementById("cancelarModal");
    const btnAbrirTarea = document.getElementById("agregarTarea");
    const modalTarea = document.getElementById("modalTarea");
    const btnCancelarTarea = document.getElementById("cancelarModalTarea");

    // Mostrar el modal para crear columna
    btnAgregar.addEventListener("click", () => {
        modal.style.display = "flex";
        inputNombre.value = "";
        inputNombre.focus();
    });

    // Cancelar modal
    btnCancelar.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Mostrar el modal para crear tarea
    btnAbrirTarea.addEventListener("click", () => {
        modalTarea.style.display = "flex";
    });

    btnCancelarTarea.addEventListener("click", () => {
        modalTarea.style.display = "none";
    });

    // Cerrar modal si se hace click fuera del contenido
    window.addEventListener("click", (e) => {
        if (e.target === modalTarea) {
            modalTarea.style.display = "none";
        }
    });

    // Crear nueva columna
    btnCrear.addEventListener("click", () => {
        const nombre = inputNombre.value.trim();
        if (nombre) {
            const nuevaColumna = document.createElement("div");
            nuevaColumna.classList.add("columna");
            nuevaColumna.setAttribute("data-etapa", nombre.toLowerCase().replace(/\s+/g, ''));
            nuevaColumna.innerHTML = `
                <h3>${nombre} <button class="eliminar-columna">🗑️</button></h3>
                <div class="tareas"></div>
                <button class="agregar-tarea">+ Añadir tarea</button>
            `;
            tableros.appendChild(nuevaColumna);
            initSortable(); // activar drag en la nueva columna
            modal.style.display = "none";
        }
    });

    // Agregar tareas
    tableros.addEventListener("click", (p) => {
        if (p.target && p.target.classList.contains("agregar-tarea")) {
            const columna = p.target.closest(".columna");
            const estado = columna.getAttribute("data-etapa");

            // Mostrar el modal para crear una nueva tarea
            modalTarea.style.display = "flex";

            // Al enviar el formulario, asignamos la tarea al contenedor correspondiente
            const formulario = modalTarea.querySelector("form");
            formulario.addEventListener("submit", (e) => {
                e.preventDefault();
                const tareaTitulo = formulario.querySelector("[name='titulo']").value;
                const tareaEstado = formulario.querySelector("[name='estado']").value;
                const tareaDiv = document.createElement("div");
                tareaDiv.classList.add("tarea");
                tareaDiv.textContent = tareaTitulo;

                // Buscamos la columna con el estado correspondiente y añadimos la tarea allí
                const columnaCorrespondiente = tableros.querySelector(`[data-etapa='${tareaEstado.toLowerCase()}']`);
                columnaCorrespondiente.querySelector(".tareas").appendChild(tareaDiv);

                modalTarea.style.display = "none"; // Cerrar el modal
            });
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