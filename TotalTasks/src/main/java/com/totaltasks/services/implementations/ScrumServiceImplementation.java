package com.totaltasks.services.implementations;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.entities.ProductBoardEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.SprintEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProductBacklogDTO;
import com.totaltasks.repositories.ProductBoardRepository;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.ScrumRepository;
import com.totaltasks.repositories.SprintRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.ScrumService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScrumServiceImplementation implements ScrumService {

	@Autowired
	private ScrumRepository scrumRepository;

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private SprintRepository sprintRepository;

	@Autowired
	private ProductBoardRepository productBoardRepository;

	@Override
	public void agregarHistoria(ProductBacklogDTO productBacklogDTO) {
		ProyectoEntity proyecto = proyectoRepository.findById(productBacklogDTO.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepository.findById(productBacklogDTO.getIdCreador()).orElse(null);

		ProductBacklogEntity entity = new ProductBacklogEntity();
		entity.setTitulo(productBacklogDTO.getTitulo());
		entity.setDescripcion(productBacklogDTO.getDescripcion());
		entity.setStoryPoints(productBacklogDTO.getStoryPoints());
		int storyPoints = productBacklogDTO.getStoryPoints();

		String prioridad;

		if (storyPoints <= 7) {
			prioridad = "Baja";
		} else if (storyPoints <= 14) {
			prioridad = "Media";
		} else {
			prioridad = "Alta";
		}

		entity.setPrioridad(prioridad);
		entity.setEstado("Por Hacer");
		entity.setProyecto(proyecto);
		entity.setCreador(usuario);

		scrumRepository.save(entity);
	}


	@Override
	public void moverHistoriaDeSprintABacklog(Long idSprint, Long idProyecto, Long idResponsable) {

		SprintEntity sprint = sprintRepository.findById(idSprint).orElse(null);

		int storyPoints = sprint.getStoryPoints();

		String prioridad;
		
		if (storyPoints <= 7) {
			prioridad = "Baja";
		} else if (storyPoints <= 14) {
			prioridad = "Media";
		} else {
			prioridad = "Alta";
		}

		ProductBacklogEntity backlogHistoria = new ProductBacklogEntity();
		backlogHistoria.setTitulo(sprint.getTitulo());
		backlogHistoria.setDescripcion(sprint.getDescripcion());
		backlogHistoria.setStoryPoints(storyPoints);
		backlogHistoria.setPrioridad(prioridad);
		backlogHistoria.setEstado("Por Hacer");
		backlogHistoria.setProyecto(sprint.getProyecto());
		backlogHistoria.setCreador(sprint.getResponsable());

		scrumRepository.save(backlogHistoria);

		sprintRepository.delete(sprint);
	}


	@Override
	public List<ProductBacklogEntity> historiasDelProyecto(Long idProyecto) {
		return scrumRepository.findByProyecto_idProyecto(idProyecto);
	}

	@Override
	public List<SprintEntity> historiasDelSprint(Long idProyecto) {
        return sprintRepository.findAllByProyecto_idProyecto(idProyecto);
    }

	@Override
	public void moverHistoriaASprint(Long idBacklog, Long idProyecto, Long idResponsable) {
		
		ProductBacklogEntity historia = scrumRepository.findById(idBacklog).orElse(null);

		int storyPoints = historia.getStoryPoints();

		String prioridad;
		
		if (storyPoints <= 7) {
			prioridad = "Baja";
		} else if (storyPoints <= 14) {
			prioridad = "Media";
		} else {
			prioridad = "Alta";
		}

		// Crear el Sprint y asignar la tarea
		SprintEntity sprint = new SprintEntity();
		sprint.setTitulo(historia.getTitulo());
		sprint.setDescripcion(historia.getDescripcion());
		sprint.setStoryPoints(historia.getStoryPoints());
		sprint.setPrioridad(prioridad);
		sprint.setEstado(historia.getEstado());
		sprint.setProyecto(historia.getProyecto());
		sprint.setResponsable(historia.getCreador());

		sprintRepository.save(sprint);

		scrumRepository.delete(historia);
	}

	@Override
	public void comenzarSprint(Long idProyecto, double duracionDias) {
		List<SprintEntity> historiasSprint = sprintRepository.findByProyecto_idProyecto(idProyecto);

		LocalDateTime fechaFinSprint = LocalDateTime.now().plusMinutes((long)(duracionDias * 1440));
		Timestamp fechaFinSprintTS = Timestamp.valueOf(fechaFinSprint);

		for (SprintEntity historia : historiasSprint) {
			int storyPoints = historia.getStoryPoints();

			String prioridad;
			if (storyPoints <= 7) {
				prioridad = "Baja";
			} else if (storyPoints <= 14) {
				prioridad = "Media";
			} else {
				prioridad = "Alta";
			}

			ProductBoardEntity tareaBoard = new ProductBoardEntity();
			tareaBoard.setTitulo(historia.getTitulo());
			tareaBoard.setDescripcion(historia.getDescripcion());
			tareaBoard.setStoryPoints(storyPoints);
			tareaBoard.setPrioridad(prioridad);
			tareaBoard.setEstado("por_hacer");
			tareaBoard.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
			tareaBoard.setProyecto(historia.getProyecto());
			tareaBoard.setResponsable(historia.getResponsable());
			tareaBoard.setFechaLimite(fechaFinSprintTS);

			productBoardRepository.save(tareaBoard);
			sprintRepository.delete(historia);
		}
	}

	public boolean estaTerminado(Long idProyecto) {
		List<SprintEntity> historias = sprintRepository.findHistoriasSprintActivas(idProyecto);

		if (historias == null || historias.isEmpty()) {
			return false;
		}

		Timestamp fechaFinSprint = historias.get(0).getFechaFin();
		return fechaFinSprint.before(new Timestamp(System.currentTimeMillis()));
	}

	@Scheduled(fixedDelay = 5000)
	public void verificarSprintFinalizados() {

		Timestamp ahora = new Timestamp(System.currentTimeMillis());

		List<ProductBoardEntity> tareas = productBoardRepository.findByEstadoNotAndFechaLimiteBefore("hecho", ahora);

		for (ProductBoardEntity tarea : tareas) {
			Integer storyPoints = tarea.getStoryPoints();
			String prioridad = tarea.getPrioridad();

			if (storyPoints == null) storyPoints = 3;

			if (prioridad == null || prioridad.isBlank()) {
				if (storyPoints <= 7) {
					prioridad = "Baja";
				} else if (storyPoints <= 14) {
					prioridad = "Media";
				} else {
					prioridad = "Alta";
				}
			}

			SprintEntity nuevaHistoria = new SprintEntity();
			nuevaHistoria.setTitulo(tarea.getTitulo());
			nuevaHistoria.setDescripcion(tarea.getDescripcion());
			nuevaHistoria.setStoryPoints(storyPoints);
			nuevaHistoria.setPrioridad(prioridad);
			nuevaHistoria.setProyecto(tarea.getProyecto());
			nuevaHistoria.setResponsable(tarea.getResponsable());
			nuevaHistoria.setEstado("pendiente");

			sprintRepository.save(nuevaHistoria);
			productBoardRepository.delete(tarea);
		}
	}

	@Override
	public void actualizarEstadoTarea(Long idTarea, String nuevoEstado) {
		ProductBoardEntity tarea = productBoardRepository.findTareaById(idTarea);
		tarea.setEstado(nuevoEstado);
		productBoardRepository.save(tarea);
	}

	@Override
	public boolean borrarTareasHechas() {
		long count = productBoardRepository.countByEstado("hecho");
		if (count > 0) {
			productBoardRepository.deleteByEstado("hecho");
			return true;
		}
		return false;
	}

}