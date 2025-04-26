document.addEventListener("DOMContentLoaded", () => {
	const menuLinks = document.querySelectorAll(".menu-link");

	// Función para scroll suave
	menuLinks.forEach((link) => {
		link.addEventListener("click", function (e) {
			e.preventDefault();
			const targetId = this.getAttribute("href");
			const targetElement = document.querySelector(targetId);
			if (targetElement) {
				window.scrollTo({
					top: targetElement.offsetTop - 80,
					behavior: "smooth",
				});
			}
			// Actualizar la clase activa
			menuLinks.forEach((item) => item.classList.remove("active"));
			this.classList.add("active");
		});
	});

	// Opcional: Resaltar automáticamente el menú según el scroll
	window.addEventListener("scroll", () => {
		let fromTop = window.scrollY + 90;
		menuLinks.forEach((link) => {
			let section = document.querySelector(link.getAttribute("href"));
			if (
				section.offsetTop <= fromTop &&
				section.offsetTop + section.offsetHeight > fromTop
			) {
				link.classList.add("active");
			} else {
				link.classList.remove("active");
			}
		});
	});
});
