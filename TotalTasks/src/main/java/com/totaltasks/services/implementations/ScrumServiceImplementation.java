package com.totaltasks.services.implementations;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.SprintEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProductBacklogDTO;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.ScrumRepository;
import com.totaltasks.repositories.SprintRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.ScrumService;

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

	@Override
	public void agregarHistoria(ProductBacklogDTO productBacklogDTO) {
		ProyectoEntity proyecto = proyectoRepository.findById(productBacklogDTO.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepository.findById(productBacklogDTO.getIdCreador()).orElse(null);

		ProductBacklogEntity entity = new ProductBacklogEntity();
		entity.setTitulo(productBacklogDTO.getTitulo());
		entity.setDescripcion(productBacklogDTO.getDescripcion());
		entity.setStoryPoints(productBacklogDTO.getStoryPoints());
		entity.setProyecto(proyecto);
		entity.setCreador(usuario);

		scrumRepository.save(entity);
	}

	@Override
	public void moverHistoriaDeSprintABacklog(Long idSprint, Long idProyecto, Long idResponsable) {
		// Primero obtenemos la historia del Sprint
		SprintEntity sprint = sprintRepository.findById(idSprint).orElseThrow(() -> new RuntimeException("Sprint no encontrado"));

		// Crear la historia en el Product Backlog
		ProductBacklogEntity backlogHistoria = new ProductBacklogEntity();
		backlogHistoria.setTitulo(sprint.getTitulo());
		backlogHistoria.setDescripcion(sprint.getDescripcion());
		backlogHistoria.setStoryPoints(0);
		backlogHistoria.setProyecto(sprint.getProyecto());
		backlogHistoria.setCreador(sprint.getResponsable());

		// Guardamos la historia de vuelta al Product Backlog
		scrumRepository.save(backlogHistoria);

		// Eliminar la historia del Sprint
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
		ProductBacklogEntity historia = scrumRepository.findById(idBacklog).orElseThrow(() -> new RuntimeException("Historia no encontrada"));

		// Crear el Sprint y asignar la tarea
		SprintEntity sprint = new SprintEntity();
		sprint.setTitulo(historia.getTitulo());
		sprint.setDescripcion(historia.getDescripcion());
		sprint.setEstado("pendiente");
		sprint.setProyecto(historia.getProyecto());
		sprint.setResponsable(historia.getCreador());

		sprintRepository.save(sprint);

		scrumRepository.delete(historia);
	}

	@Override
	public void comenzarSprint(Long idProyecto) {
		// Actualiza el estado de las historias dentro del sprint, o lo que sea necesario.
	}
}