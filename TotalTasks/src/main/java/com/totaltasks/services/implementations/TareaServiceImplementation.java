package com.totaltasks.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.models.TareaDTO;
import com.totaltasks.entities.TareaEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.TareaRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.TablonService;
import com.totaltasks.services.TareaService;

@Service
public class TareaServiceImplementation implements TareaService {

	@Autowired
	private TareaRepository tareaRepository;

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private TablonService tablonService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void crearTarea(TareaDTO dto) {

		TareaEntity tarea = new TareaEntity();

		ProyectoEntity proyecto = proyectoRepository.findById(dto.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepository.findById(dto.getIdResponsable()).orElse(null);

		tarea.setTitulo(dto.getTitulo());
		tarea.setDescripcion(dto.getDescripcion());
		tarea.setFechaLimite(dto.getFechaLimite());
		tarea.setEstado(tablonService.ordenarTablones(proyecto.getTablones()).get(0).getNombreTablon());
		tarea.setProyecto(proyecto);
		tarea.setResponsable(usuario);

		tareaRepository.save(tarea);
	}

	@Override
	public String modificarEstadoTarea(TareaDTO tareaDTO) {

		TareaEntity tareaEntity = tareaRepository.findById(tareaDTO.getIdTarea()).orElse(null);

		tareaEntity.setEstado(tareaDTO.getEstado());
		tareaRepository.save(tareaEntity);

		return "Tarea Modificada";
	}

}