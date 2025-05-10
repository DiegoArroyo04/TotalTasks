package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.models.ProductBacklogDTO;

public interface ScrumService {
    void agregarHistoria(ProductBacklogDTO productBacklogDTO);

	public List<ProductBacklogEntity> historiasDelProyecto(Long idProyecto);
}
