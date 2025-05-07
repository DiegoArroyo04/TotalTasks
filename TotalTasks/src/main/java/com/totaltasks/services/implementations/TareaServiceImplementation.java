package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.models.TareaDTO;
import com.totaltasks.entities.TareaEntity;
import com.totaltasks.entities.NotificacionEntity;
import com.totaltasks.entities.NotificacionUsuarioEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.repositories.NotificacionRepository;
import com.totaltasks.repositories.NotificacionUsuarioRepository;
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
	private NotificacionRepository notificacionRepository;

	@Autowired
	private NotificacionUsuarioRepository notificacionUsuarioRepository;

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private TablonService tablonService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public boolean crearTarea(TareaDTO dto) {
		TareaEntity tarea = new TareaEntity();

		ProyectoEntity proyecto = proyectoRepository.findById(dto.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepository.findById(dto.getIdResponsable()).orElse(null);

		List<TablonEntity> tablonesOrdenados = tablonService.ordenarTablones(proyecto.getTablones());

		if (tablonesOrdenados == null || tablonesOrdenados.isEmpty()) {
			// No hay tablones → no podemos crear la tarea
			return false;
		}

		tarea.setTitulo(dto.getTitulo());
		tarea.setDescripcion(dto.getDescripcion());
		tarea.setFechaLimite(dto.getFechaLimite());
		tarea.setEstado(tablonesOrdenados.get(0).getNombreTablon());
		tarea.setProyecto(proyecto);
		tarea.setResponsable(usuario);

		tareaRepository.save(tarea);

		// CREAR NOTIFICACION PARA EL ADMINISTRADOR
		NotificacionEntity notificacionEntity = new NotificacionEntity();
		notificacionEntity.setProyecto(proyecto);
		notificacionEntity.setTarea(tarea);
		notificacionEntity.setTipo("ADMIN_MODIFICACION");
		notificacionEntity.setMensaje(
				"El usuario " + tarea.getResponsable().getUsuario() + " ha creado la tarea " + tarea.getTitulo());

		NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
		notificacionUsuarioEntity.setNotificacion(notificacionEntity);
		notificacionUsuarioEntity.setDestinatario(proyecto.getCreador());

		notificacionRepository.save(notificacionEntity);
		notificacionUsuarioRepository.save(notificacionUsuarioEntity);

		return true;
	}

	@Override
	public String modificarEstadoTarea(TareaDTO tareaDTO) {

		TareaEntity tareaEntity = tareaRepository.findById(tareaDTO.getIdTarea()).orElse(null);

		tareaEntity.setEstado(tareaDTO.getEstado());
		tareaRepository.save(tareaEntity);

		return "Tarea Modificada";
	}

}