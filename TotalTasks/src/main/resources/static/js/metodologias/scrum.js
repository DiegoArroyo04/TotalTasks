document.addEventListener("DOMContentLoaded", function () {
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
				error: function (xhr, status, error) {
					console.error("❌ Error al mover historias:", error);
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
				error: function (xhr, status, error) {
					console.error(
						"❌ Error al mover historia de Sprint al Product Backlog:",
						error
					);
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
		error: function (xhr, status, error) {
			console.error("❌ Error al comenzar el Sprint:", error);
		},
	});
}
