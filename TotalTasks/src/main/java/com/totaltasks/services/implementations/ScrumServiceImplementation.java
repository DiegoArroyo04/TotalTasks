package com.totaltasks.services.implementations;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProductBacklogDTO;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.ScrumRepository;
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
	public List<ProductBacklogEntity> historiasDelProyecto(Long idProyecto) {
		return scrumRepository.findByProyecto_idProyecto(idProyecto);
	}

}
