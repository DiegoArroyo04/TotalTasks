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

	document.getElementById("botonEstadisticas").addEventListener("click", () => {
		const form = document.getElementById("formularioGithub");
		const modal = document.getElementById("modalEstadisticas");

		if (!form || !modal) return;

		modal.classList.add("activo");

		// Por defecto mostramos estadísticas de los commits
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

	// Botones de filtro (commits, lenguajes, pull requests)
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
		} else if (tipo === "pullRequests") {
			url = `/api/github/pullRequests?owner=${owner}&repoName=${repo}`;
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
				alert("Error al cargar datos de estadísticas tipo: " + tipo);
			}
		});
	}

	function renderizarGrafico(tipo, datos) {

		const contenedor = document.querySelector("#modalEstadisticas .modal-contenido");

		// Eliminar canvas anterior si existe
		const viejoCanvas = document.getElementById("graficoGithub");
		if (viejoCanvas) viejoCanvas.remove();

		// Crear y agregar nuevo canvas
		const canvas = document.createElement("canvas");
		canvas.id = "graficoGithub";
		contenedor.appendChild(canvas);

		const ctx = canvas.getContext("2d");

		// Destruir gráfico anterior si existe
		if (chart) chart.destroy();

		if (tipo === "commits") {
			const autores = datos.map(commit => commit.commit.author.name);

			const conteo = autores.reduce((acc, autor) => {
				acc[autor] = (acc[autor] || 0) + 1;
				return acc;
			}, {});

			const labels = Object.keys(conteo);
			const valores = Object.values(conteo);

			const chart = new Chart(ctx, {
				type: "bar",
				data: {
					labels: labels,
					datasets: [{
						label: "Commits por autor",
						data: valores,
						backgroundColor: "rgba(75, 192, 192, 0.2)",
						borderColor: "rgba(75, 192, 192, 1)",
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

		else if (tipo === "pullRequests") {
			const abiertos = datos.filter(pr => pr.state === "open").length;
			const cerrados = datos.filter(pr => pr.state === "closed").length;

			chart = new Chart(ctx, {
				type: "doughnut",
				data: {
					labels: ["Abiertos", "Cerrados"],
					datasets: [{
						label: "Pull Requests",
						data: [abiertos, cerrados],
						backgroundColor: ["#36a2eb", "#ff6384"]
					}],
				},
				options: {
					responsive: true
				}
			});
		}
	}

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

	// Sortable para el Product Backlog (ya existente)
	const sortableBacklog = new Sortable(backlogTable, {
		group: "shared",
		animation: 150,
		onEnd(evt) {
			const item = evt.item;
			const idBacklog = item.dataset.id;
			const idProyecto = item.dataset.proyectoId;
			const idResponsable = item.dataset.responsableId;

			$.ajax({
				url: "/scrum/moverHistoriaASprint",
				type: "POST",
				data: {
					idBacklog: idBacklog,
					idProyecto: idProyecto,
					idResponsable: idResponsable,
				},
				success: function (respuesta) {
					location.reload();
				},
			});
		},
	});

	// Sortable para el Sprint
	const sortableSprint = new Sortable(sprintTable, {
		group: "shared",
		animation: 150,
		onEnd(evt) {
			const item = evt.item;
			const idSprint = item.dataset.id;
			const idProyecto = item.dataset.proyectoId;
			const idResponsable = item.dataset.responsableId;

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
		},
	});
});

function comenzarSprint() {
	const idProyecto = document.querySelector("[data-proyecto-id]")?.dataset.proyectoId;

	if (!idProyecto) {
		alert("No se pudo determinar el proyecto.");
		return;
	}

	$.ajax({
		url: "/scrum/comenzarSprint",
		type: "POST",
		data: { idProyecto: idProyecto },
		success: function () {
			location.reload();
		},
	});
}