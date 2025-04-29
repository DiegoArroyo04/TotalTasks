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
	const botonEliminarColumna = document.getElementById(
		"botonEliminarColumna"
	);
	let columnaArrastrada = null;



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

	btnCrear.addEventListener("click", () => {
		const nombre = inputNombre.value.trim();
		if (nombre) {
			const idProyecto = document.getElementById("idProyecto").value;
			const orden = document.getElementById("orden").value;

			$.ajax({
				url: "/crearTablon",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify({
					nombreTablon: nombre,
					orden: parseInt(orden) + 1,
					id_proyecto: idProyecto,
				}),

				success: function (response) {
					if (response == "Duplicado") {

						modal.style.display = "none";
						const modalError = document.getElementById("modalError");
						const mensajeElem = document.getElementById("mensajeError");
						mensajeElem.textContent = "Este tablón ya existe.";
						modalError.style.display = "flex";


					} else {
						// Crear columna en el DOM
						const nuevaColumna = document.createElement("div");
						nuevaColumna.classList.add("columna");
						nuevaColumna.setAttribute(
							"data-etapa",
							nombre.toLowerCase().replace(/\s+/g, "")
						);
						nuevaColumna.innerHTML = `
                <h3>${nombre}</h3>
                <div class="tareas"></div>
            `;

						nuevaColumna.setAttribute("data-id", String(response));
						tableros.appendChild(nuevaColumna);
						initSortableCol(); // activar drag en la nueva columna
						modal.style.display = "none";
						console.log("Columna creada con éxito");
					}

				},
				error: function (xhr, status, error) {
					console.error("❌ Error al crear columna:", error);
				},
			});
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
				const tareaTitulo =
					formulario.querySelector("[name='titulo']").value;
				const tareaEstado =
					formulario.querySelector("[name='estado']").value;
				const tareaDiv = document.createElement("div");
				tareaDiv.classList.add("tarea");
				tareaDiv.textContent = tareaTitulo;

				// Buscamos la columna con el estado correspondiente y añadimos la tarea allí
				const columnaCorrespondiente = tableros.querySelector(
					`[data-etapa='${tareaEstado.toLowerCase()}']`
				);
				columnaCorrespondiente
					.querySelector(".tareas")
					.appendChild(tareaDiv);

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

	function initSortable() {
		const listas = document.querySelectorAll(".tareas");
		listas.forEach((lista) => {
			new Sortable(lista, {
				group: "kanban",
				animation: 200,
				ghostClass: "ghost",
				onAdd: function (evento) {
					const tareaMovida = evento.item;
					const idTarea = tareaMovida.getAttribute("data-id");

					//COLUMNA A LA QUE SE HA MOVIDO
					const nuevaColumna = evento.to.closest(".columna");

					const nuevoEstado = nuevaColumna.getAttribute("data-etapa");

					var datos = {
						idTarea: idTarea,
						estado: nuevoEstado,
					};

					$.ajax({
						url: "/actualizarEstadoTarea",
						type: "POST",
						contentType: "application/json",
						data: JSON.stringify(datos),
						success: function (respuesta) { },
						error: function (xhr, status, error) {
							console.error(
								"❌ Error al actualizar tarea:",
								error
							);
						},
					});
				},
			});
		});
	}

	initSortable();

	function initSortableCol() {
		new Sortable(tableros, {
			animation: 150,
			handle: "div", // para arrastrar desde el título
			ghostClass: "ghost-columna",
			onStart: function (evt) {
				columnaArrastrada = evt.item;
				botonEliminarColumna.style.display = "block";
			},
			onEnd: function (evt) {
				const papelera = botonEliminarColumna.getBoundingClientRect();
				const mouseX = evt.originalEvent.clientX;
				const mouseY = evt.originalEvent.clientY;

				//VERIFICACION DE QUE EL EVENTO NO SE HA HECHO PARA ELIMINAR EL TABLON ENTERA
				if (
					mouseX >= papelera.left &&
					mouseX <= papelera.right &&
					mouseY >= papelera.top &&
					mouseY <= papelera.bottom
				) {
					const idProyecto =
						document.getElementById("idProyecto").value;
					const estado = columnaArrastrada.getAttribute("data-etapa");

					$.ajax({
						url: "/eliminarTablon",
						method: "POST",
						contentType: "application/json",
						data: JSON.stringify({
							nombreTablon: estado,
							id_proyecto: parseInt(idProyecto),
						}),
						success: function () {
							columnaArrastrada.remove(); // eliminar del DOM
							actualizarOrdenTablones();

						},
						error: function (xhr, status, error) {
							console.error(
								"❌ Error al eliminar columna:",
								error
							);
						},
					});
				}
				// Ocultar el botón de eliminar columna siempre que se suelta
				setTimeout(() => {
					botonEliminarColumna.style.display = "none";
					columnaArrastrada = null;
				}, 200);

				actualizarOrdenTablones();
			},
		});
	}

	initSortableCol();

});

function cerrarModalError() {
	document.getElementById("modalError").style.display = "none";
}

function actualizarOrdenTablones() {
	const columnas = document.querySelectorAll(".columna");
	const ordenActualizado = [];

	columnas.forEach((columna, i) => {
		const idTablon = columna.getAttribute("data-id");
		if (idTablon) {
			ordenActualizado.push({
				id: parseInt(idTablon),
				orden: i + 1,
			});
		}
	});

	$.ajax({
		url: "/actualizarOrdenTablones",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(ordenActualizado),
		success: function () {
			console.log("✅ Orden de columnas actualizado");
		},
		error: function (xhr, status, error) {
			console.error(
				"❌ Error al actualizar orden de columnas:",
				error
			);
		},
	});
}
