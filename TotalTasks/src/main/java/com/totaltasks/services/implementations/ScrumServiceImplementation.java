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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

		// Crear el Sprint y asignar la tarea
		SprintEntity sprint = new SprintEntity();
		sprint.setTitulo(historia.getTitulo());
		sprint.setDescripcion(historia.getDescripcion());
		sprint.setStoryPoints(historia.getStoryPoints());
		sprint.setPrioridad(historia.getPrioridad());
		sprint.setEstado(historia.getEstado());
		sprint.setProyecto(historia.getProyecto());
		sprint.setResponsable(historia.getCreador());

		sprintRepository.save(sprint);

		scrumRepository.delete(historia);
	}

	@Override
	public void comenzarSprint(Long idProyecto) {
		List<SprintEntity> historiasSprint = sprintRepository.findByProyecto_idProyecto(idProyecto);

		for (SprintEntity historia : historiasSprint) {
			ProductBoardEntity tareaBoard = new ProductBoardEntity();
			tareaBoard.setTitulo(historia.getTitulo());
			tareaBoard.setDescripcion(historia.getDescripcion());
			tareaBoard.setEstado("por_hacer");
			tareaBoard.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
			tareaBoard.setProyecto(historia.getProyecto());
			tareaBoard.setResponsable(historia.getResponsable());

			productBoardRepository.save(tareaBoard);

			sprintRepository.delete(historia);
		}
	}

}