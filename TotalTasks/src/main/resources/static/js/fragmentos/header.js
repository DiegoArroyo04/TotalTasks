window.addEventListener("scroll", () => {
    const header = document.getElementById("cabezera");
    const spacer = document.getElementById("headerSpacer");
    const headerHeight = header.offsetHeight;

    if (window.scrollY > 120) {
        header.classList.add("fixed");
        spacer.style.height = `${headerHeight}px`;
    } else {
        header.classList.remove("fixed");
        spacer.style.height = '0px';
    }
});