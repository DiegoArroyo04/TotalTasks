// Añadir tarea al Product Backlog
document.getElementById('task-form').addEventListener('submit', e => {
    e.preventDefault();
    const title = document.getElementById('task-title').value;
    const desc = document.getElementById('task-desc').value;
    const points = document.getElementById('task-points').value;

    const li = createTaskElement(title, desc, points);
    li.draggable = true;
    li.addEventListener('dragstart', dragStart);
    document.getElementById('backlog-list').appendChild(li);

    updateRetroTaskOptions();
    e.target.reset();
});

// Crear elemento de tarea
function createTaskElement(title, desc, points) {
    const li = document.createElement('li');
    li.className = 'task';
    li.textContent = `${title} (${points} pts)`;
    li.dataset.title = title;
    li.dataset.desc = desc;
    li.dataset.points = points;
    return li;
}

// Drag & Drop
document.querySelectorAll('#sprint-list, .task-list').forEach(list => {
    list.addEventListener('dragover', e => e.preventDefault());
    list.addEventListener('drop', e => {
        e.preventDefault();
        const id = e.dataTransfer.getData('text/plain');
        const taskOriginal = document.getElementById(id);

        // Evitar duplicados en sprint
        if (list.id === 'sprint-list' && [...list.children].some(li => li.dataset.title === taskOriginal.dataset.title)) {
            alert('Esta tarea ya está en el sprint backlog');
            return;
        }

        // Clonar para sprint backlog y scrum board
        const taskClone = taskOriginal.cloneNode(true);
        taskClone.id = id + '-clone' + Math.random().toString(36).substr(2,4);
        taskClone.draggable = true;
        taskClone.addEventListener('dragstart', dragStart);
        list.appendChild(taskClone);
    });
});

function dragStart(e) {
    const id = 'task-' + Math.random().toString(36).substr(2, 9);
    e.target.id = id;
    e.dataTransfer.setData('text/plain', id);
}

// Actualizar opciones select retrospectiva
function updateRetroTaskOptions() {
    const select = document.getElementById('retro-task');
    select.innerHTML = '';
    document.querySelectorAll('#sprint-list .task').forEach(task => {
        const opt = document.createElement('option');
        opt.value = task.dataset.title;
        opt.textContent = task.dataset.title;
        select.appendChild(opt);
    });
}

// Añadir comentario retrospectiva
document.getElementById('retro-form').addEventListener('submit', e => {
    e.preventDefault();
    const tarea = document.getElementById('retro-task').value;
    const autor = document.getElementById('retro-author').value;
    const comentario = document.getElementById('retro-comment').value;
    const fecha = new Date().toLocaleString();

    const li = document.createElement('li');
    li.className = 'retro-item';
    li.innerHTML = `<strong>${tarea}</strong> - ${autor} (${fecha}):<br>${comentario}`;

    document.getElementById('retro-list').appendChild(li);
    e.target.reset();
});