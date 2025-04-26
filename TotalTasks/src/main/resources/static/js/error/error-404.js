document.addEventListener("DOMContentLoaded", function () {
	const cerebro = document.querySelector(".emoji-cerebro");

	const secuenciaBurbujas = [
		document.querySelector(".burbuja-1"),
		document.querySelector(".burbuja-2"),
		document.querySelector(".burbuja-3"),
		document.querySelector(".burbuja-4"),
		document.querySelector(".burbuja-5"),
		document.querySelector(".burbuja-6"),
		document.querySelector(".burbuja-7"),
		document.querySelector(".burbuja-8"),
		document.querySelector(".burbuja-9"),
		document.querySelector(".burbuja-10"),
	];

	let indice = 0;

	function mostrarBurbuja() {
		if (indice >= secuenciaBurbujas.length) {
			setTimeout(() => {
				indice = 0;
				mostrarBurbuja();
			}, 3000);
			return;
		}

		const burbuja = secuenciaBurbujas[indice];

		const esRoja = burbuja.classList.contains("burbuja-roja");
		const esVerde = burbuja.classList.contains("burbuja-verde");

		// Mostrar burbuja
		burbuja.style.opacity = "1";
		burbuja.style.transform = "scale(1)";
		burbuja.style.animation = "none";
		burbuja.offsetHeight;
		burbuja.style.animation = "brainPulse 3s ease-in-out infinite";

		// Efecto de brillo en cerebro
		if (esRoja) {
			cerebro.classList.add("glow-rojo");
		} else if (esVerde) {
			cerebro.classList.add("glow-verde");
		}

		setTimeout(() => {
			burbuja.style.opacity = "0";
			burbuja.style.transform = "scale(0.8)";
			cerebro.classList.remove("glow-rojo", "glow-verde");

			setTimeout(() => {
				indice++;
				mostrarBurbuja();
			}, 1000);
		}, 5000);
	}

	setTimeout(() => {
		mostrarBurbuja();
	}, 6000);
});