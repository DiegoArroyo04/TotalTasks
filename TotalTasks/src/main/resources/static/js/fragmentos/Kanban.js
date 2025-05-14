document.addEventListener("DOMContentLoaded", () => {
  //LEER SI HAY COLORES PERSONALIZADOS
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

      //VERIFICAR SI YA EXISTE UN SPAN CUSTOM PARA MODIFICAR SUS VALORES
      if (customBox) {
        customBox.dataset.color = customColor;
        customBox.style.backgroundColor = customColor;
      } else {
        if (customColor != null) {
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
      }

      if (colorGuardado && hoverGuardado) {
        document.documentElement.style.setProperty(
          "--color-primario",
          colorGuardado
        );
        document.documentElement.style.setProperty(
          "--color-primario-hover",
          hoverGuardado
        );
      }
    },
    error: function (error) {},
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
            const nuevoInput = nuevaColumna.querySelector(
              "input.titulo-tablon"
            );
            activarEdicion(nuevoInput);

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
        const tareaTitulo = formulario.querySelector("[name='titulo']").value;
        const tareaEstado = formulario.querySelector("[name='estado']").value;
        const tareaDiv = document.createElement("div");
        tareaDiv.classList.add("tarea");
        tareaDiv.textContent = tareaTitulo;

        // Buscamos la columna con el estado correspondiente y añadimos la tarea allí
        const columnaCorrespondiente = tableros.querySelector(
          `[data-etapa='${tareaEstado.toLowerCase()}']`
        );
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
            success: function (respuesta) {},
            error: function (xhr, status, error) {
              console.error("❌ Error al actualizar tarea:", error);
            },
          });
        },
      });
    });
  }

  initSortable();

  function initSortableCol() {
    let ordenInicial = [];

    new Sortable(tableros, {
      animation: 150,
      handle: "div", // para arrastrar desde el título
      ghostClass: "ghost-columna",
      onStart: function (evt) {
        columnaArrastrada = evt.item;
        botonEliminarColumna.style.display = "block";

        // Guardar el orden actual
        ordenInicial = Array.from(tableros.children).map((col) =>
          col.getAttribute("data-id")
        );
      },
      onEnd: function (evt) {
        const papelera = botonEliminarColumna.getBoundingClientRect();
        const mouseX = evt.originalEvent.clientX;
        const mouseY = evt.originalEvent.clientY;
        let eliminado = false;

        //VERIFICACION DE QUE EL EVENTO NO SE HA HECHO PARA ELIMINAR EL TABLON ENTERA
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
              columnaArrastrada.remove(); // eliminar del DOM
              actualizarOrdenTablones();
            },
            error: function (xhr, status, error) {
              console.error("❌ Error al eliminar columna:", error);
            },
          });
        }

        // COMPROBAR QUE EL ORDEN DE LOS TABLONES A CAMBIADO
        const nuevoOrden = Array.from(tableros.children).map((col) =>
          col.getAttribute("data-id")
        );
        const haCambiado = ordenInicial.some(
          (id, index) => id !== nuevoOrden[index]
        );
        if (haCambiado && eliminado == false) {
          actualizarOrdenTablones();
        }

        // Ocultar el botón de eliminar columna siempre que se suelta
        setTimeout(() => {
          botonEliminarColumna.style.display = "none";
          columnaArrastrada = null;
        }, 200);
      },
    });
  }

  initSortableCol();

  document
    .getElementById("personalizarTablero")
    .addEventListener("click", function () {
      document.getElementById("colorPicker").style.display = "flex";

      document
        .getElementById("personalizarColor")
        .addEventListener("click", function () {
          document.getElementById("colorSelector").click();
        });

      //EVENTO QUE ESCUCHA EL NUEVO COLOR PERSONALIZADO
      document
        .getElementById("colorSelector")
        .addEventListener("change", function (event) {
          const color = event.target.value;
          const container = document.getElementById("color-options");

          // Verificar si ya existe un color con ese valor en el contenedor
          const colorOptions = document.getElementById("color-options");
          if (!colorOptions.querySelector(`span[data-color='${color}']`)) {
            let customBox = container.querySelector(
              "span.color-box.custom-box"
            );

            //VERIFICAR SI YA EXISTE UN SPAN CUSTOM PARA MODIFICAR SUS VALORES
            if (customBox) {
              customBox.dataset.color = color;
              customBox.style.backgroundColor = color;
            } else {
              // Crear el nuevo <span> con el color seleccionado
              const colorBox = document.createElement("span");
              colorBox.classList.add("color-box", "custom-box");
              colorBox.setAttribute("data-color", color);
              colorBox.style.backgroundColor = color;

              // Agregar el nuevo <span> al contenedor de colores
              colorOptions.insertBefore(
                colorBox,
                document.getElementById("personalizarColor")
              );
            }

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
              success: function () {
                console.log("✅ Color personalizado guardado");
              },
              error: function () {
                console.error("❌ Error al guardar el color personalizado");
              },
            });
          }
        });
    });

  document.addEventListener("click", function (event) {
    // Cerrar al hacer clic fuera del colorPicker
    const pickerContainer = document.getElementById("colorPicker");
    const pickerBox = document.querySelector(".colorPicker");

    const isClickInside = pickerBox.contains(event.target);
    const isButton = event.target.id === "personalizarTablero";

    if (!isClickInside && !isButton) {
      pickerContainer.style.display = "none";
    }

    //EVENT LISTENER PARA CADA COLOR
    document.querySelectorAll(".color-box").forEach((box) => {
      box.addEventListener("click", () => {
        const color = box.getAttribute("data-color");
        document.documentElement.style.setProperty("--color-primario", color);
        const hoverColor = oscurecerColor(color, 0.15);
        document.documentElement.style.setProperty(
          "--color-primario-hover",
          hoverColor
        );
        const idProyecto = document.getElementById("idProyecto").value;

        //GUARDAR COLOR EN BBDD
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

          success: function (response) {},
          error: function (xhr, status, error) {},
        });

        // Cerrar el selector
        document.getElementById("colorPicker").style.display = "none";
      });
    });
  });

  //CALENDARIO DE GOOGLE
  document
    .getElementById("googleCalendarBtn")
    ?.addEventListener("click", function () {
      const titulo = document
        .querySelector('input[name="titulo"]')
        .value.trim();
      const descripcion = document
        .querySelector('textarea[name="descripcion"]')
        .value.trim();
      const fecha = document.querySelector('input[name="fechaLimite"]').value;

      if (!titulo) {
        const modalError = document.getElementById("modalError");
        const mensajeElem = document.getElementById("mensajeError");
        mensajeElem.textContent =
          "Por favor completa el titulo para poder crear el recordatorio.";
        modalError.style.display = "flex";
        return;
      }

      if (!fecha) {
        const modalError = document.getElementById("modalError");
        const mensajeElem = document.getElementById("mensajeError");
        mensajeElem.textContent =
          "Por favor completa la fecha para poder crear el recordatorio.";
        modalError.style.display = "flex";
        return;
      }

      const startDate = fecha.replace(/-/g, "") + "T090000Z";
      const endDate = fecha.replace(/-/g, "") + "T100000Z";

      const url = `https://calendar.google.com/calendar/render?action=TEMPLATE&text=${encodeURIComponent(
        titulo
      )}&dates=${startDate}/${endDate}&details=${encodeURIComponent(
        descripcion
      )}`;

      window.open(url, "_blank");
    });

  //CALENDARIO

  $.ajax({
    type: "GET",
    url: `/obtenerTareasPorUserYProyecto?usuarioId=${idUsuario}&proyectoId=${idProyecto}`,
    dataType: "json",
    success: function (data) {
      const tareas = data;

      // Solo pinta el calendario si hay más de una tarea
      if (tareas.length >= 1) {
        const eventos = tareas.map((t) => ({
          id: t.idTarea,
          title: t.titulo,
          start: new Date(t.fechaLimite),
          description: t.descripcion,
        }));

        const calendarEl = document.getElementById("calendar");
        const calendar = new FullCalendar.Calendar(calendarEl, {
          initialView: "dayGridMonth",
          editable: true,
          height: "auto",
          events: eventos,
          eventDrop: function (info) {
            //AJAX PARA MODIFICAR FECHA TAREA
            const nuevaFecha = info.event.start.toISOString().split("T")[0]; // Formatear Fecha
            const idTarea = info.event.id;

            // FORMATO PARA TABLONES
            const fechaFormateada = info.event.start.toLocaleDateString(
              "es-ES",
              {
                day: "2-digit",
                month: "2-digit",
                year: "numeric",
              }
            );

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
                  // Verifica si la tarea NO está dentro del calendario
                  if (
                    !tarea.closest("#calendar") &&
                    tarea.getAttribute("data-id") === idTarea
                  ) {
                    const parrafos = tarea.querySelectorAll("p");
                    //RECORREMOS SUS PARAFOS Y EN EL ULTIMO DENTRO DE SMALL COLOCAMOS LA NUEVA FECHA
                    for (let i = parrafos.length - 1; i >= 0; i--) {
                      if (parrafos[i].querySelector("small")) {
                        parrafos[i].querySelector("small").innerHTML =
                          fechaFormateada;
                        break;
                      }
                    }
                  }
                });
              },
              error: function (xhr, status, error) {
                console.error(
                  "❌ Error al actualizar la fecha de la tarea:",
                  error
                );
              },
            });
          },
          eventClick: function (info) {
            alert(
              "Título: " +
                info.event.title +
                "\nDescripción: " +
                info.event.extendedProps.description
            );
          },
          eventClassNames: function (info) {
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
    error: function (error) {
      console.log(error);
    },
  });
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
    success: function () {
      console.log("✅ Orden de columnas actualizado");
    },
    error: function (xhr, status, error) {
      console.error("❌ Error al actualizar orden de columnas:", error);
    },
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
              data ==
              "No se pudo actualizar (tablero no encontrado o nombre duplicado)"
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

    input.addEventListener(
      "keypress",
      function (e) {
        if (e.key === "Enter") input.blur();
      },
      { once: true }
    );
  });
}

// Activar doble click en todos los inputs iniciales
document
  .querySelectorAll("#tableros .columna input.titulo-tablon")
  .forEach(activarEdicion);
