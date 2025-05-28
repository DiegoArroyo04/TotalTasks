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