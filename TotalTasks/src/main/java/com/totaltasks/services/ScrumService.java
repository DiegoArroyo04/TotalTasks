package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.entities.SprintEntity;
import com.totaltasks.models.ProductBacklogDTO;

public interface ScrumService {
    void agregarHistoria(ProductBacklogDTO productBacklogDTO);

	public List<ProductBacklogEntity> historiasDelProyecto(Long idProyecto);

	void moverHistoriaASprint(Long idBacklog, Long idProyecto, Long idResponsable);

	void comenzarSprint(Long idProyecto);

	List<SprintEntity> historiasDelSprint(Long idProyecto);

	void moverHistoriaDeSprintABacklog(Long idSprint, Long idProyecto, Long idResponsable);
}
