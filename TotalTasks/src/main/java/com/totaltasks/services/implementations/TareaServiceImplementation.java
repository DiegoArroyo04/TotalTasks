package com.totaltasks.services.implementations;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

		// CREAR NOTIFICACION PARA EL USUARIO QUE SE LE HA ASIGNADO LA TAREA
		NotificacionEntity notificacionUser = new NotificacionEntity();
		notificacionUser.setProyecto(proyecto);
		notificacionUser.setTarea(tarea);
		notificacionUser.setTipo("TAREA_ASIGNADA");
		notificacionUser.setMensaje(
				"Se te ha asignado la tarea " + tarea.getTitulo());

		NotificacionUsuarioEntity notificacionUsuarioUser = new NotificacionUsuarioEntity();
		notificacionUsuarioUser.setNotificacion(notificacionUser);
		notificacionUsuarioUser.setDestinatario(tarea.getResponsable());

		notificacionRepository.save(notificacionUser);
		notificacionUsuarioRepository.save(notificacionUsuarioUser);

		return true;

	}

	@Override
	public String modificarEstadoTarea(TareaDTO tareaDTO, UsuarioEntity usuario) {

		TareaEntity tareaEntity = tareaRepository.findById(tareaDTO.getIdTarea()).orElse(null);

		if (usuario != null) {
			// CREAR NOTIFICACION PARA EL ADMINISTRADOR
			NotificacionEntity notificacionEntity = new NotificacionEntity();
			notificacionEntity.setProyecto(tareaEntity.getProyecto());
			notificacionEntity.setTarea(tareaEntity);
			notificacionEntity.setTipo("ADMIN_MODIFICACION");
			notificacionEntity.setMensaje(
					"El usuario " + usuario.getNombre() + " ha movido la tarea de " + tareaEntity.getEstado() + " a "
							+ tareaDTO.getEstado());

			NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
			notificacionUsuarioEntity.setNotificacion(notificacionEntity);
			notificacionUsuarioEntity.setDestinatario(tareaEntity.getProyecto().getCreador());

			notificacionRepository.save(notificacionEntity);
			notificacionUsuarioRepository.save(notificacionUsuarioEntity);
		}

		tareaEntity.setEstado(tareaDTO.getEstado());
		tareaRepository.save(tareaEntity);

		return "Tarea Modificada";
	}

	@Override
	public void verificarTareasProximas(UsuarioEntity usuario) {

		// Obtener la fecha de hoy y la fecha en dos días
		LocalDate hoy = LocalDate.now();
		LocalDate fechaLimite = hoy.plusDays(2);

		// Filtrar las tareas que tienen una fecha límite dentro de hoy y dos días
		List<TareaEntity> tareasProximas = usuario.getTareasAsignadas().stream()
				.filter(t -> {
					// Obtener la fecha de límite de la tarea
					LocalDate fechaTarea = t.getFechaLimite().toLocalDate();
					// Verificar si la fecha de la tarea está en el rango de hoy y dos días
					return !fechaTarea.isBefore(hoy) && !fechaTarea.isAfter(fechaLimite);
				})
				.collect(Collectors.toList());

		for (TareaEntity tarea : tareasProximas) {
			crearRecordatorioFecha(tarea);
		}
	}

	@Override
	public void crearRecordatorioFecha(TareaEntity tarea) {
		// NOTIFICACION PARA RECORDAR QUE TIENE TAREAS A PUNTO DE VENCER
		NotificacionEntity notificacion = new NotificacionEntity();
		notificacion.setProyecto(tarea.getProyecto());
		notificacion.setTarea(tarea);
		notificacion.setTipo("RECORDATORIO_FECHA");
		notificacion
				.setMensaje("La tarea '" + tarea.getTitulo() + "' está próxima a vencer el " + tarea.getFechaLimite());

		NotificacionUsuarioEntity notificacionUsuario = new NotificacionUsuarioEntity();
		notificacionUsuario.setNotificacion(notificacion);
		notificacionUsuario.setDestinatario(tarea.getResponsable());

		notificacionRepository.save(notificacion);
		notificacionUsuarioRepository.save(notificacionUsuario);

		// CREAR NOTIFICACION PARA EL ADMINISTRADOR
		NotificacionEntity notificacionEntity = new NotificacionEntity();
		notificacionEntity.setProyecto(tarea.getProyecto());
		notificacionEntity.setTarea(tarea);
		notificacionEntity.setTipo("RECORDATORIO_FECHA");
		notificacionEntity.setMensaje(
				"El usuario " + tarea.getResponsable().getNombre() + " tiene pendiente la tarea " + tarea.getTitulo()
						+ " y finaliza  "
						+ tarea.getFechaLimite());

		NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
		notificacionUsuarioEntity.setNotificacion(notificacionEntity);
		notificacionUsuarioEntity.setDestinatario(tarea.getProyecto().getCreador());

		notificacionRepository.save(notificacionEntity);
		notificacionUsuarioRepository.save(notificacionUsuarioEntity);

	}

	@Override
	public List<TareaEntity> obtenerTareasPorUserYProyecto(Long usuarioId, Long proyectoId) {

		UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioId).orElse(null);

		for (ProyectoEntity proyecto : usuarioEntity.getProyectos()) {
			if (proyecto.getIdProyecto().equals(proyectoId)) {
				return proyecto.getTareas();
			}
		}

		return Collections.emptyList();

	}

	@Override
	public void actualizarFechaTarea(Long idTarea, Date nuevaFecha,Long idUsuario) {
		TareaEntity tareaEntity=tareaRepository.findById(idTarea).orElse(null);
		tareaEntity.setFechaLimite(nuevaFecha);
		tareaRepository.save(tareaEntity);
		UsuarioEntity usuario=usuarioRepository.findById(idUsuario).orElse(null);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaFormateada = sdf.format(nuevaFecha);	


		// CREAR NOTIFICACION PARA EL ADMINISTRADOR
		NotificacionEntity notificacionEntity = new NotificacionEntity();
		notificacionEntity.setProyecto(tareaEntity.getProyecto());
		notificacionEntity.setTarea(tareaEntity);
		notificacionEntity.setTipo("ADMIN_MODIFICACION");
		notificacionEntity.setMensaje(
				"El usuario " + usuario.getNombre() + " ha cambiado la fecha de finalizacion de la tarea  " + tareaEntity.getTitulo()
						+ " a el dia  "
						+ fechaFormateada);

		NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
		notificacionUsuarioEntity.setNotificacion(notificacionEntity);
		notificacionUsuarioEntity.setDestinatario(tareaEntity.getProyecto().getCreador());

		notificacionRepository.save(notificacionEntity);
		notificacionUsuarioRepository.save(notificacionUsuarioEntity);


		// CREAR NOTIFICACION PARA EL RESPONSABLE DE LA TAREA
		NotificacionEntity notificacionEntityResponsable = new NotificacionEntity();
		notificacionEntityResponsable.setProyecto(tareaEntity.getProyecto());
		notificacionEntityResponsable.setTarea(tareaEntity);
		notificacionEntityResponsable.setTipo("ADMIN_MODIFICACION");
		notificacionEntityResponsable.setMensaje(
				"El usuario " + usuario.getNombre() + " ha cambiado la fecha de finalizacion de la tarea  " + tareaEntity.getTitulo()
						+ " a el dia  "
						+ fechaFormateada);

		NotificacionUsuarioEntity notificacionUsuarioEntityResponsable = new NotificacionUsuarioEntity();
		notificacionUsuarioEntityResponsable.setNotificacion(notificacionEntityResponsable);
		notificacionUsuarioEntityResponsable.setDestinatario(tareaEntity.getResponsable());

		notificacionRepository.save(notificacionEntityResponsable);
		notificacionUsuarioRepository.save(notificacionUsuarioEntityResponsable);






	}

}