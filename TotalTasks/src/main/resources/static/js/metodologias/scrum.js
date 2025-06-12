document.addEventListener("DOMContentLoaded", function () {

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

	let chart = null; // Gráfico global para poder destruirlo

	document.getElementById("botonEstadisticas")?.addEventListener("click", () => {
		const form = document.getElementById("formularioGithub");
		const modal = document.getElementById("modalEstadisticas");

		if (!form || !modal) return;

		modal.classList.add("activo");

		// Mostrar loader
		document.getElementById("loader").style.display = "block";

		cargarEstadisticas("commits");

	});

	document.getElementById("cerrarModalEstadisticas")?.addEventListener("click", () => {
		document.getElementById("modalEstadisticas")?.classList.remove("activo");

		// Destruir gráfico si existe
		if (chart) {
			chart.destroy();
			chart = null;
		}

	});

	// Botones de filtro (commits, lenguajes,ramas)
	document.querySelectorAll(".filtro-btn").forEach(boton => {
		boton.addEventListener("click", () => {
			const tipo = boton.dataset.filtro;
			cargarEstadisticas(tipo);
		});
	});

	function cargarEstadisticas(tipo) {

		const owner = document.getElementById("repoFullName").value.split("/")[0];
		const repo = document.getElementById("repoName").value;

		let url = "";

		if (tipo === "commits") {
			url = `/api/github/extraerCommits?owner=${owner}&repoName=${repo}`;
		} else if (tipo === "lenguajes") {
			url = `/api/github/stats/lenguajes?owner=${owner}&repoName=${repo}`;
		} else if (tipo === "branches") {
			url = `/api/github/branches?owner=${owner}&repoName=${repo}`;
		} else {
			return;
		}

		$.ajax({
			url: url,
			method: "GET",
			contentType: "application/json",
			success: function (data) {
				renderizarGrafico(tipo, data);

			},
			error: function () {

			}
		});
	}

	function renderizarGrafico(tipo, datos) {

		// Ocultar loader antes de pintar el gráfico
		document.getElementById("loader").style.display = "none";

		const contenedor = document.querySelector("#modalEstadisticas .modal-contenido");

		// Eliminar canvas anterior si existe
		const viejoCanvas = document.getElementById("graficoGithub");
		if (viejoCanvas) viejoCanvas.remove();

		// Crear y agregar nuevo canvas
		const canvas = document.createElement("canvas");
		canvas.id = "graficoGithub";
		canvas.style.width = "600px";
		canvas.style.maxHeight = "400px";
		canvas.style.display = "block";
		canvas.style.margin = "0 auto";

		contenedor.appendChild(canvas);

		const ctx = canvas.getContext("2d");

		// Destruir gráfico anterior si existe
		if (chart) chart.destroy();

		if (tipo === "commits") {
			const autores = datos.map(commit => commit.author?.login || commit.commit.author.name);

			const conteo = autores.reduce((acc, autor) => {
				acc[autor] = (acc[autor] || 0) + 1;
				return acc;
			}, {});

			const labels = Object.keys(conteo);
			const valores = Object.values(conteo);

			const colorPrimario = getComputedStyle(document.documentElement).getPropertyValue("--color-primario").trim();
			const colorHover = getComputedStyle(document.documentElement).getPropertyValue("--color-primario-hover").trim();

			const chart = new Chart(ctx, {
				type: "bar",
				data: {
					labels: labels,
					datasets: [{
						label: "Commits por autor",
						data: valores,
						backgroundColor: colorPrimario,
						borderColor: colorHover,
						borderWidth: 1
					}]
				},
				options: {
					responsive: true,
					indexAxis: 'y',
					scales: {
						x: {
							beginAtZero: true,
							title: {
								display: true,
								text: "Número de commits"
							}
						},
						y: {
							title: {
								display: true,
								text: "Autor"
							}
						}
					}
				}
			});
		}

		else if (tipo === "lenguajes") {
			const labels = Object.keys(datos);
			const valores = Object.values(datos);

			chart = new Chart(ctx, {
				type: "pie",
				data: {
					labels: labels,
					datasets: [{
						label: "Lenguajes utilizados",
						data: valores,
						backgroundColor: ["#ff6384", "#36a2eb", "#ffcd56", "#4bc0c0", "#9966ff"]
					}],
				},
				options: {
					responsive: true
				}
			});
		}
		else if (tipo === "branches") {
			if (!Array.isArray(datos) || datos.length === 0) {

				document.querySelector('button[data-filtro="branches"]').style.display = "none";
				return;
			}

			const nombres = datos.map(branch => branch.name);
			const colores = [
				"#4bc0c0", "#36a2eb", "#9966ff", "#ff6384", "#ffcd56", "#c9cbcf", "#00d084"
			];

			chart = new Chart(ctx, {
				type: "bar",
				data: {
					labels: nombres,
					datasets: [{
						label: "Ramas del repositorio",
						data: nombres.map(() => Math.floor(Math.random() * 5) + 1),
						backgroundColor: colores,
					}]
				},
				options: {
					indexAxis: "y",
					responsive: true,
					scales: {
						x: {
							display: false
						},
						y: {
							ticks: {
								font: {
									size: 14,
									weight: 'bold'
								}
							},
							title: {
								display: true,
								text: "Nombre de la rama"
							}
						}
					},
					plugins: {
						legend: { display: false },
						tooltip: {
							callbacks: {
								label: function (context) {
									return `Rama: ${context.label}`;
								}
							}
						}
					}
				}
			});

		}


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

const backlogTable = document.querySelector("#product-backlog table tbody");
const sprintTable = document.querySelector("#sprint-table tbody");

// === Sortable para el Product Backlog (drag and drop) ===
const sortableBacklog = new Sortable(backlogTable, {
	group: "shared",
	animation: 150,
	onEnd(evt) {
		const item = evt.item;
		const idBacklog = item.dataset.id;
		const idProyecto = item.dataset.proyectoId;
		const idResponsable = item.dataset.responsableId;

		moverHistoriaAlSprint(idBacklog, idProyecto, idResponsable);
	},
});

// === Sortable para el Sprint (drag and drop) ===
const sortableSprint = new Sortable(sprintTable, {
	group: "shared",
	animation: 150,
	onEnd(evt) {
		const item = evt.item;
		const idSprint = item.dataset.id;
		const idProyecto = item.dataset.proyectoId;
		const idResponsable = item.dataset.responsableId;

		moverHistoriaDeSprintABacklog(idSprint, idProyecto, idResponsable);
	},
});

// === Función reutilizable para mover historia al Sprint ===
function moverHistoriaAlSprint(idBacklog, idProyecto, idResponsable) {
	$.ajax({
		url: "/scrum/moverHistoriaASprint",
		type: "POST",
		data: {
			idBacklog: idBacklog,
			idProyecto: idProyecto,
			idResponsable: idResponsable,
		},
		success: function (respuesta) {
			location.reload(); // puedes reemplazarlo por actualizar el DOM dinámicamente
		},
	});
}

// === Función para mover historia del Sprint al Backlog ===
function moverHistoriaDeSprintABacklog(idSprint, idProyecto, idResponsable) {
	$.ajax({
		url: "/scrum/moverDeSprintABacklog",
		type: "POST",
		data: {
			idSprint: idSprint,
			idProyecto: idProyecto,
			idResponsable: idResponsable,
		},
		success: function (respuesta) {
			location.reload();
		},
	});
}

function moverTodoAlBacklog() {
	const historias = document.querySelectorAll('#sprint-table tbody tr');
	let pendientes = historias.length;

	historias.forEach(historia => {
		const id = historia.dataset.id;
		const idProyecto = historia.dataset.proyectoId;
		const idResponsable = historia.dataset.responsableId;

		$.ajax({
			url: "/scrum/moverDeSprintABacklog",
			type: "POST",
			data: {
				idSprint: id,
				idProyecto: idProyecto,
				idResponsable: idResponsable,
			},
			complete: function () {
				pendientes--;
				if (pendientes === 0) {
					location.reload();
				}
			},
		});
	});
}

// === Función para mover todas las historias del backlog al sprint (botón) ===
function moverAlSprint() {
	const historias = document.querySelectorAll('#product-backlog tbody tr');

	historias.forEach(historia => {
		const id = historia.dataset.id;
		const idProyecto = historia.dataset.proyectoId;
		const idResponsable = historia.dataset.responsableId;

		moverHistoriaAlSprint(id, idProyecto, idResponsable);
	});
}

function comenzarSprint() {
	document.getElementById("modal-sprint").style.display = "flex";
}

function cerrarModal() {
	document.getElementById("modal-sprint").style.display = "none";
}

function confirmarComenzarSprint() {
	const idProyecto = document.querySelector("[data-proyecto-id]")?.dataset.proyectoId;
	const duracion = parseInt(document.getElementById("duracion").value);
	const unidad = document.getElementById("unidad-tiempo").value;

	if (!idProyecto || !duracion || duracion < 1) {

		return;
	}

	let duracionDias;
	switch (unidad) {
		case "segundos": duracionDias = duracion / 86400; break;
		case "minutos": duracionDias = duracion / 1440; break;
		case "horas": duracionDias = duracion / 24; break;
		default: duracionDias = duracion;
	}
	duracionDias


	$.ajax({
		url: "/scrum/comenzarSprint",
		type: "POST",
		data: {
			idProyecto: idProyecto,
			duracionDias: duracionDias,
		},
		success: function () {
			location.reload();
		},
	});
}

function verificarSprintTerminado(idProyecto) {
	$.ajax({
		url: "/scrum/sprintTerminado",
		type: "GET",
		data: { idProyecto: idProyecto },
		success: function (terminado) {
			if (terminado) {
				mostrarModalSprintTerminado();
			}
		},
		error: function (err) {

		}
	});
}

// === Inicializar columnas del Product Board con SortableJS ===
['por-hacer', 'en-curso', 'hecho'].forEach(columnId => { //inicializar ids de cada li
	new Sortable(document.getElementById(columnId), {
		group: 'tareas',
		animation: 150,
		onAdd: function (evt) {
			const item = evt.item;
			const nuevaColumna = evt.to.id; // por-hacer, en-curso, hecho
			const idTarea = item.dataset.id;

			let nuevoEstado = "";
			switch (nuevaColumna) {
				case "por-hacer":
					nuevoEstado = "por_hacer";
					break;
				case "en-curso":
					nuevoEstado = "en_curso";
					break;
				case "hecho":
					nuevoEstado = "hecho";
					break;
			}

			$.ajax({
				url: "/scrum/actualizarEstadoTarea",
				type: "POST",
				data: {
					idTarea: idTarea,
					nuevoEstado: nuevoEstado
				},
			});
		}
	});
});

const btnBorrarHecho = document.getElementById('btn-borrar-hecho');
if (btnBorrarHecho) {
	btnBorrarHecho.addEventListener('click', function () {
		$.ajax({
			url: "/scrum/borrarTareasHechas",
			type: "POST",
			success: function (response) {
				// Eliminar todas las tareas de la columna "hecho"
				const listaHecho = document.getElementById('hecho');
				if (listaHecho) {
					listaHecho.innerHTML = ""; // Borra todos los <li>
				}
			},
			error: function (err) {


			}
		});
	});
}