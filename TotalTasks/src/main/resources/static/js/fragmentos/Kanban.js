document.addEventListener("DOMContentLoaded", () => {

	// LEER SI HAY COLORES PERSONALIZADOS
	const idProyecto = document.getElementById("idProyecto").value;
	const idUsuario = document.getElementById("idUsuario").value;

	$.ajax({
		type: "GET",
		url: `/obtenerColores?usuarioId=${idUsuario}&proyectoId=${idProyecto}`,
		success: function (data) {
			const colorGuardado = data.color;
			const hoverGuardado = data.colorHover;
			const customColor = data.customColor;

			const container = document.getElementById("color-options");
			const colorOptions = document.getElementById("color-options");
			let customBox = container.querySelector("span.color-box.custom-box");

			// VERIFICAR SI YA EXISTE UN SPAN CUSTOM PARA MODIFICAR SUS VALORES
			if (customBox) {
				customBox.dataset.color = customColor;
				customBox.style.backgroundColor = customColor;
			} else if (customColor != null) {
				// Crear el nuevo <span> con el color seleccionado
				const colorBox = document.createElement("span");
				colorBox.classList.add("color-box", "custom-box");
				colorBox.setAttribute("data-color", customColor);
				colorBox.style.backgroundColor = customColor;

				// Agregar el nuevo <span> al contenedor de colores
				colorOptions.insertBefore(
					colorBox,
					document.getElementById("personalizarColor")
				);
			}

			if (colorGuardado && hoverGuardado) {
				document.documentElement.style.setProperty("--color-primario", colorGuardado);
				document.documentElement.style.setProperty("--color-primario-hover", hoverGuardado);
			}
		},
	});

	const tableros = document.getElementById("tableros");
	const btnAgregar = document.getElementById("agregarColumna");
	const modal = document.getElementById("modalColumna");
	const inputNombre = document.getElementById("nombreColumna");
	const btnCrear = document.getElementById("crearColumna");
	const btnCancelar = document.getElementById("cancelarModal");
	const btnAbrirTarea = document.getElementById("agregarTarea");
	const modalTarea = document.getElementById("modalTarea");
	const btnCancelarTarea = document.getElementById("cancelarModalTarea");
	const botonEliminarColumna = document.getElementById("botonEliminarColumna");
	let columnaArrastrada = null;
	let tarea = null;
	let calendar;


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
							<input type="text" value="${nombre}" class="titulo-tablon" readonly />
							<div class="tareas"></div>
						`;
						nuevaColumna.setAttribute("data-id", String(response));
						tableros.appendChild(nuevaColumna);
						initSortableCol(); // activar drag en la nueva columna

						// 🔧 ACTIVAR EDICIÓN EN EL NUEVO INPUT
						const nuevoInput = nuevaColumna.querySelector("input.titulo-tablon");
						activarEdicion(nuevoInput);

						modal.style.display = "none";
					}
				},
			});
		}
	});

	// Obtener todas las tareas
	const tareas = document.querySelectorAll(".tarea");
	tareas.forEach((tarea) => {
		tarea.addEventListener("click", () => {
			const id = tarea.getAttribute("data-id");
			const titulo = tarea.getAttribute("data-titulo");
			const descripcion = tarea.getAttribute("data-descripcion");
			const fechaLimite = tarea.getAttribute("data-fecha");
			const responsableId = tarea.getAttribute("data-responsable");

			// Rellenar los campos del modal de edición
			document.getElementById("idTareaEditar").value = id;
			document.getElementById("tituloEditar").value = titulo;
			document.getElementById("descripcionEditar").value = descripcion;
			document.getElementById("fechaLimiteEditar").value = fechaLimite;
			document.getElementById("idResponsableEditar").value = responsableId;

			// También rellenamos el modal informativo si lo usas antes de editar
			document.getElementById("tituloTarea").innerText = titulo;
			document.getElementById("descripcionTarea").innerText = descripcion;
			document.getElementById("responsableTarea").innerText = tarea.querySelector("p:nth-child(3)")?.innerText?.replace("Asignado a ", "") || "";
			document.getElementById("fechaLimiteTarea").innerText = fechaLimite;

			// Mostrar modal de detalle
			document.getElementById("modalMostrarTarea").style.display = "flex";
		});
	});

	document.getElementById("modificarTarea").addEventListener("click", function () {
		document.getElementById("modalEditarTarea").style.display = "flex";
	});

	// Cerrar modal de detalle
	document.getElementById("cerrarModalTarea").addEventListener("click", () => {
		document.getElementById("modalMostrarTarea").style.display = "none";
	});

	// Cancelar modal de edición
	document.getElementById("cancelarModalEditarTarea").addEventListener("click", () => {
		document.getElementById("modalEditarTarea").style.display = "none";
	});

	// Agregar tareas desde columna
	tableros.addEventListener("click", (p) => {
		if (p.target && p.target.classList.contains("agregar-tarea")) {
			const columna = p.target.closest(".columna");
			const estado = columna.getAttribute("data-etapa");

			modalTarea.style.display = "flex";

			const formulario = modalTarea.querySelector("form");
			formulario.addEventListener("submit", (e) => {
				e.preventDefault();
				const tareaTitulo = formulario.querySelector("[name='titulo']").value;
				const tareaEstado = formulario.querySelector("[name='estado']").value;
				const tareaDiv = document.createElement("div");
				tareaDiv.classList.add("tarea");
				tareaDiv.textContent = tareaTitulo;

				const columnaCorrespondiente = tableros.querySelector(
					`[data-etapa='${tareaEstado.toLowerCase()}']`
				);
				columnaCorrespondiente.querySelector(".tareas").appendChild(tareaDiv);

				modalTarea.style.display = "none";
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

	// Función para arrastrar tareas
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
						success: function () { },
					});
				},
				onStart: function (evt) {
					tarea = evt.item;
					botonEliminarColumna.style.display = "block";
				},
				onEnd: function (evt) {
					const papelera = botonEliminarColumna.getBoundingClientRect();
					const mouseX = evt.originalEvent.clientX;
					const mouseY = evt.originalEvent.clientY;

					if (mouseX >= papelera.left && mouseX <= papelera.right && mouseY >= papelera.top && mouseY <= papelera.bottom) {
						const idTarea = tarea.getAttribute("data-id");
						const idUsuario = document.getElementById("idUsuario").value;
						const idProyecto = document.getElementById("idProyecto").value;

						$.ajax({
							url: `/eliminarTarea/${idTarea}/${idUsuario}/${idProyecto}`,
							method: "POST",
							success: function () {
								tarea.remove();
								const evento = calendar.getEventById(idTarea);

								if (evento) evento.remove();

								// Ocultar calendario si no quedan eventos
								if (calendar.getEvents().length === 0) {
									document.getElementById("calendar").style.display = "none";
								}
							},
						});
					}

					setTimeout(() => {
						botonEliminarColumna.style.display = "none";
						columnaArrastrada = null;
					}, 200);
				}
			});
		});
	}

	initSortable();

	function initSortableCol() {
		let ordenInicial = [];

		new Sortable(tableros, {
			animation: 150,
			handle: "div", // Solo se puede arrastrar desde el título de la columna
			ghostClass: "ghost-columna",

			onStart: function (evt) {
				columnaArrastrada = evt.item;
				botonEliminarColumna.style.display = "block";

				// Guardar el orden actual de los tablones
				ordenInicial = Array.from(tableros.children).map((col) =>
					col.getAttribute("data-id")
				);
			},

			onEnd: function (evt) {
				const papelera = botonEliminarColumna.getBoundingClientRect();
				const mouseX = evt.originalEvent.clientX;
				const mouseY = evt.originalEvent.clientY;
				let eliminado = false;

				// Verificar si el soltado fue sobre la papelera
				if (
					mouseX >= papelera.left &&
					mouseX <= papelera.right &&
					mouseY >= papelera.top &&
					mouseY <= papelera.bottom
				) {
					const idProyecto = document.getElementById("idProyecto").value;
					const estado = columnaArrastrada.getAttribute("data-etapa");
					eliminado = true;

					$.ajax({
						url: "/eliminarTablon",
						method: "POST",
						contentType: "application/json",
						data: JSON.stringify({
							nombreTablon: estado,
							id_proyecto: parseInt(idProyecto),
						}),
						success: function () {
							// Eliminar la columna del DOM y actualizar el orden
							columnaArrastrada.remove();
							actualizarOrdenTablones();
						},
					});
				}

				// Comprobar si el orden de los tablones ha cambiado
				const nuevoOrden = Array.from(tableros.children).map((col) =>
					col.getAttribute("data-id")
				);

				const haCambiado = ordenInicial.some(
					(id, index) => id !== nuevoOrden[index]
				);

				if (haCambiado && eliminado === false) {
					actualizarOrdenTablones();
				}

				// Ocultar botón de eliminar y limpiar referencia
				setTimeout(() => {
					botonEliminarColumna.style.display = "none";
					columnaArrastrada = null;
				}, 200);
			},
		});
	}

	initSortableCol();

	document.getElementById("personalizarTablero").addEventListener("click", function () {
		// Mostrar el selector de color
		document.getElementById("colorPicker").style.display = "flex";

		// Abrir el input tipo color al hacer clic en el botón
		document.getElementById("personalizarColor").addEventListener("click", function () {
			document.getElementById("colorSelector").click();
		});

		// Cambiar color en tiempo real
		document.getElementById("colorSelector").addEventListener("input", function (event) {
			const color = event.target.value;

			// Aplicar a las variables CSS personalizadas
			document.documentElement.style.setProperty("--color-primario", color);
			document.documentElement.style.setProperty("--color-primario-hover", oscurecerColor(color, 0.15));

			// Vista previa en el span personalizado
			let customBox = document.querySelector("span.color-box.custom-box");
			if (customBox) {
				customBox.style.backgroundColor = color;
				customBox.dataset.color = color;
			}
		});

		// Guardar el nuevo color personalizado
		document.getElementById("colorSelector").addEventListener("change", function (event) {
			const color = event.target.value;
			const container = document.getElementById("color-options");

			// Verificar si ya existe un span con ese color
			const colorOptions = document.getElementById("color-options");
			if (!colorOptions.querySelector(`span[data-color='${color}']`)) {
				let customBox = container.querySelector("span.color-box.custom-box");

				if (customBox) {
					// Actualizar el span existente
					customBox.dataset.color = color;
					customBox.style.backgroundColor = color;
				} else {
					// Crear un nuevo span con el color seleccionado
					const colorBox = document.createElement("span");
					colorBox.classList.add("color-box", "custom-box");
					colorBox.setAttribute("data-color", color);
					colorBox.style.backgroundColor = color;

					// Insertar el nuevo span antes del botón personalizar
					colorOptions.insertBefore(
						colorBox,
						document.getElementById("personalizarColor")
					);
				}

				// Guardar el color personalizado vía AJAX
				const idProyecto = document.getElementById("idProyecto").value;
				const idUsuario = document.getElementById("idUsuario").value;

				$.ajax({
					url: "/guardarColores",
					type: "POST",
					contentType: "application/json",
					data: JSON.stringify({
						idUsuario: idUsuario,
						idProyecto: idProyecto,
						customColor: color,
					}),
				});
			}
		});
	});

	document.addEventListener("click", function (event) {
		// Cerrar el colorPicker si se hace clic fuera de él y no es el botón que lo abre
		const pickerContainer = document.getElementById("colorPicker");
		const pickerBox = document.querySelector(".colorPicker");

		const isClickInside = pickerBox.contains(event.target);
		const isButton = event.target.id === "personalizarTablero";

		if (!isClickInside && !isButton) {
			pickerContainer.style.display = "none";
		}

		// Agregar evento click a cada caja de color para cambiar el tema
		document.querySelectorAll(".color-box").forEach((box) => {
			box.addEventListener("click", () => {
				const color = box.getAttribute("data-color");
				document.documentElement.style.setProperty("--color-primario", color);

				const hoverColor = oscurecerColor(color, 0.15);
				document.documentElement.style.setProperty("--color-primario-hover", hoverColor);

				const idProyecto = document.getElementById("idProyecto").value;
				const idUsuario = document.getElementById("idUsuario").value;

				// Guardar el color seleccionado en la base de datos mediante AJAX
				$.ajax({
					url: "/guardarColores",
					type: "POST",
					contentType: "application/json",
					data: JSON.stringify({
						idUsuario: idUsuario,
						idProyecto: idProyecto,
						color: color,
						colorHover: hoverColor,
					}),
				});
				// Cerrar el selector de color tras seleccionar uno
				pickerContainer.style.display = "none";
			});
		});
	});

	// CALENDARIO DE GOOGLE
	document.querySelectorAll(".btn-calendar")?.forEach((btn) => {
		btn.addEventListener("click", function () {
			const form = btn.closest("form");
			if (!form) return;

			const titulo = form.querySelector('input[name="titulo"]')?.value.trim();
			const descripcion = form.querySelector('textarea[name="descripcion"]')?.value.trim();
			const fecha = form.querySelector('input[name="fechaLimite"]')?.value;

			// Validar campos obligatorios
			if (!titulo || !fecha) {
				const modalError = document.getElementById("modalError");
				const mensajeElem = document.getElementById("mensajeError");

				mensajeElem.textContent = !titulo ? "Por favor completa el título para poder crear el recordatorio." : "Por favor completa la fecha para poder crear el recordatorio.";
				modalError.style.display = "flex";

				return;
			}

			// Formatear fechas para URL de Google Calendar (formato YYYYMMDDTHHMMSSZ)
			const startDate = fecha.replace(/-/g, "") + "T090000Z";
			const endDate = fecha.replace(/-/g, "") + "T100000Z";

			// Construir URL para crear evento en Google Calendar
			const url = `https://calendar.google.com/calendar/render?action=TEMPLATE&text=${encodeURIComponent(titulo)}&dates=${startDate}/${endDate}&details=${encodeURIComponent(descripcion)}`;

			// Abrir en nueva pestaña
			window.open(url, "_blank");
		});
	});

	// CALENDARIO
	$.ajax({
		type: "GET",
		url: `/obtenerTareasPorUserYProyecto?usuarioId=${idUsuario}&proyectoId=${idProyecto}`,
		dataType: "json",
		success: function (data) {
			const tareas = data;
			// Solo pinta el calendario si hay al menos una tarea
			if (tareas.length >= 1) {
				const eventos = tareas.map((t) => ({
					id: t.idTarea,
					title: t.titulo,
					start: new Date(t.fechaLimite),
					description: t.descripcion,
				}));

				const calendarEl = document.getElementById("calendar");
				calendar = new FullCalendar.Calendar(calendarEl, {
					initialView: "dayGridMonth",
					editable: true,
					height: "auto",
					events: eventos,

					eventDrop: function (info) {
						// AJAX para modificar fecha de tarea
						const nuevaFecha = info.event.start.toISOString().split("T")[0]; // Formatear fecha
						const idTarea = info.event.id;

						// Formato para tablones (dd/mm/yyyy)
						const fechaFormateada = info.event.start.toLocaleDateString("es-ES", {
							day: "2-digit",
							month: "2-digit",
							year: "numeric",
						});

						$.ajax({
							url: "/cambiarFechaTarea",
							method: "POST",
							data: {
								idUsuario: idUsuario,
								idTarea: idTarea,
								nuevaFecha: nuevaFecha,
							},
							success: function () {
								// Actualiza la fecha en el DOM para las tareas visibles (fuera del calendario)
								document.querySelectorAll(".tarea").forEach((tarea) => {
									if (!tarea.closest("#calendar") && tarea.getAttribute("data-id") === idTarea) {
										const parrafos = tarea.querySelectorAll("p");
										// Actualiza el <small> con la nueva fecha en el último párrafo relevante
										for (let i = parrafos.length - 1; i >= 0; i--) {
											if (parrafos[i].querySelector("small")) {
												parrafos[i].querySelector("small").innerHTML = fechaFormateada;
												break;
											}
										}
									}
								});
							},
						});
					},

					eventClick: function (info) {
						const idTarea = info.event.id;
						// Buscar tarea fuera del calendario con mismo id para mostrar detalles
						document.querySelectorAll(".tarea").forEach((tarea) => {
							if (!tarea.closest("#calendar") && tarea.getAttribute("data-id") === idTarea) {

								const parrafos = tarea.querySelectorAll("p");
								const titulo = parrafos[0]?.querySelector("strong")?.innerText || "Título no disponible";
								const descripcion = tarea.querySelector(".descripcion")?.innerText || "Descripción no disponible";
								const responsable = parrafos[2]?.innerText?.replace("Asignado a ", "") || "Responsable no disponible";
								const fechaLimite = parrafos[3]?.querySelector("small")?.innerText || "Fecha límite no disponible";

								// Asignar información al modal
								document.getElementById("tituloTarea").innerText = titulo;
								document.getElementById("descripcionTarea").innerText = descripcion;
								document.getElementById("responsableTarea").innerText = responsable;
								document.getElementById("fechaLimiteTarea").innerText = fechaLimite;

								// Mostrar el modal
								document.getElementById("modalMostrarTarea").style.display = "flex";
							}
						});
					},

					eventClassNames: function () {
						return "tarea";
					},

					eventContent: function (info) {
						return {
							html: `<strong>${info.event.title}</strong>`,
						};
					},
				});

				calendar.render();
			}
		},
	});


	document.getElementById("botonEstadisticas").addEventListener("click", () => {
		const form = document.getElementById("formularioGithub");
		const modal = document.getElementById("modalEstadisticas");

		if (!form || !modal) {
			return;
		}

		const datos = {
			name: document.getElementById("repoName").value,
			fullName: document.getElementById("repoFullName").value,
			description: document.getElementById("repoDescription").value,
			htmlUrl: document.getElementById("repoHtmlUrl").value,
			homepage: document.getElementById("repoHomepage").value,
			language: document.getElementById("repoLanguage").value,
			stars: +document.getElementById("repoStargazers").value,
			forks: +document.getElementById("repoForks").value,
			watchers: +document.getElementById("repoWatchers").value,
			issues: +document.getElementById("repoOpenIssues").value,
			createdAt: document.getElementById("repoCreatedAt").value,
			updatedAt: document.getElementById("repoUpdatedAt").value,
			pushedAt: document.getElementById("repoPushedAt").value,
		};

		modal.classList.add("activo");
		generarGraficoConDatos(datos);
	});

	document.getElementById("cerrarModalEstadisticas")?.addEventListener("click", () => {
		document.getElementById("modalEstadisticas")?.classList.remove("activo");
	});

	// Función para generar el gráfico dentro del modal
	function generarGraficoConDatos(datos) {
		const contenedor = document.querySelector("#modalEstadisticas .modal-contenido");

		// Eliminar canvas anterior si existe
		const viejoCanvas = document.getElementById("graficoGithub");
		if (viejoCanvas) viejoCanvas.remove();

		// Crear y agregar nuevo canvas
		const canvas = document.createElement("canvas");
		canvas.id = "graficoGithub";
		contenedor.appendChild(canvas);

		const ctx = canvas.getContext("2d");

		new Chart(ctx, {
			type: "bar",
			data: {
				labels: ["Stars", "Forks", "Watchers", "Issues"],
				datasets: [{
					label: "Estadísticas del repositorio",
					data: [datos.stars, datos.forks, datos.watchers, datos.issues],
					backgroundColor: ["#ff6384", "#36a2eb", "#ffcd56", "#4bc0c0"],
				}],
			},
			options: {
				responsive: true,
				scales: {
					y: {
						beginAtZero: true,
					},
				},
			},
		});
	}

});

// Función para oscurecer un color hexadecimal
function oscurecerColor(hex, porcentaje) {
	let r = parseInt(hex.slice(1, 3), 16);
	let g = parseInt(hex.slice(3, 5), 16);
	let b = parseInt(hex.slice(5, 7), 16);

	r = Math.floor(r * (1 - porcentaje));
	g = Math.floor(g * (1 - porcentaje));
	b = Math.floor(b * (1 - porcentaje));

	return `#${[r, g, b].map((x) => x.toString(16).padStart(2, "0")).join("")}`;
}

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
	});
}

function activarEdicion(input) {
	input.addEventListener("click", function () {
		const columna = this.closest(".columna");
		const idColumna = columna.getAttribute("data-id");
		const nombreActual = this.value;
		this.readOnly = false;
		this.focus();

		function salirEdicion(nuevoNombre, exitoso) {
			input.readOnly = true;
			input.value = nuevoNombre;
			if (!exitoso) {
				const modalError = document.getElementById("modalError");
				const mensajeElem = document.getElementById("mensajeError");
				mensajeElem.textContent = "Ya existe un tablon con ese nombre.";
				modalError.style.display = "flex";
			}
		}

		input.addEventListener("blur", function () {
			const nuevoNombre = input.value.trim();
			if (nuevoNombre && nuevoNombre !== nombreActual) {
				$.ajax({
					url: "/actualizarNombreTablon",
					method: "POST",
					contentType: "application/json",
					data: JSON.stringify({
						id: parseInt(idColumna),
						nombreTablon: nuevoNombre,
					}),
					success: function (data) {
						if (
							data == "No se pudo actualizar (tablero no encontrado o nombre duplicado)"
						) {
							salirEdicion(nombreActual, false);
						} else {
							salirEdicion(nuevoNombre, true);
						}
					},
					error: function () {
						salirEdicion(nombreActual, false);
					},
				});
			} else {
				salirEdicion(nombreActual, true);
			}
		});

		input.addEventListener("keypress", function (e) {
			if (e.key === "Enter") input.blur();
		},
			{ once: true }
		);
	});
}

// Activar doble click en todos los inputs iniciales
document.querySelectorAll("#tableros .columna input.titulo-tablon").forEach(activarEdicion);