package com.totaltasks.services.implementations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.NotificacionEntity;
import com.totaltasks.entities.NotificacionUsuarioEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.entities.TareaEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.models.UsuarioProyectoDTO;
import com.totaltasks.repositories.NotificacionRepository;
import com.totaltasks.repositories.NotificacionUsuarioRepository;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.TablonRepository;
import com.totaltasks.repositories.TareaRepository;
import com.totaltasks.repositories.UsuarioProyectoRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.TablonService;
import com.totaltasks.services.TareaService;

import jakarta.transaction.Transactional;

@Service
public class TablonServiceImplementation implements TablonService {

	@Autowired
	private TablonRepository tablonRepository;

	@Autowired
	private TareaRepository tareaRepository;

	@Autowired
	private TareaService tareaService;

	@Autowired
	private UsuarioProyectoRepository usuarioProyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private NotificacionRepository notificacionRepository;

	@Autowired
	private NotificacionUsuarioRepository notificacionUsuarioRepository;

	@Override
	public Long crearTablon(TablonDTO tablonDTO, UsuarioEntity usuario) {

		ProyectoEntity proyecto = tablonDTO.getProyecto();
		boolean tablonExistente = false;

		// RECORRER TABLONES DEL PROYECTO COMPROBANDO QUE YA EXISTA
		for (int i = 0; i < proyecto.getTablones().size(); i++) {
			if (proyecto.getTablones().get(i).getNombreTablon().equals(tablonDTO.getNombreTablon())) {
				tablonExistente = true;
			}
		}

		if (tablonExistente) {
			return null;
		} else {
			TablonEntity tablonEntity = new TablonEntity();
			tablonEntity.setNombreTablon(tablonDTO.getNombreTablon());
			tablonEntity.setOrden(tablonDTO.getOrden());
			tablonEntity.setProyecto(tablonDTO.getProyecto());
			tablonRepository.save(tablonEntity);

			// SI EXISTE EL USUARIO QUE CREO EL TABLON PUES CREAMOS NOTIFICACION
			if (usuario != null) {
				// CREAR NOTIFICACION PARA EL ADMINISTRADOR
				NotificacionEntity notificacionEntity = new NotificacionEntity();
				notificacionEntity.setProyecto(proyecto);
				notificacionEntity.setTipo("ADMIN_MODIFICACION");
				notificacionEntity.setMensaje("El usuario " + usuario.getNombre() + " ha creado el tablon  " + tablonEntity.getNombreTablon());

				NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
				notificacionUsuarioEntity.setNotificacion(notificacionEntity);
				notificacionUsuarioEntity.setDestinatario(proyecto.getCreador());

				notificacionRepository.save(notificacionEntity);
				notificacionUsuarioRepository.save(notificacionUsuarioEntity);
			}

			return tablonEntity.getId();
		}

	}

	@Override
	public List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar) {
		listaSinOrdenar.sort(Comparator.comparing(TablonEntity::getOrden));
		return listaSinOrdenar;
	}

	@Override
	public String actualizarOrdenTablones(List<TablonDTO> tablonOrden, UsuarioEntity usuario) {
		for (TablonDTO dto : tablonOrden) {
			TablonEntity tablon = tablonRepository.findById(dto.getId()).orElse(null);
			if (tablon != null) {
				tablon.setOrden(dto.getOrden());
				tablonRepository.save(tablon);

				// SI EXISTE EL USUARIO QUE ACTUALIZO EL TABLON PUES CREAMOS NOTIFICACION
				if (usuario != null) {
					// CREAR NOTIFICACION PARA EL ADMINISTRADOR
					NotificacionEntity notificacionEntity = new NotificacionEntity();
					notificacionEntity.setProyecto(tablon.getProyecto());
					notificacionEntity.setTipo("ADMIN_MODIFICACION");
					notificacionEntity.setMensaje("El usuario " + usuario.getNombre() + " ha cambiado el orden del tablon  " + tablon.getNombreTablon() + " ahora se encuentra en la posición " + tablon.getOrden());

					NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
					notificacionUsuarioEntity.setNotificacion(notificacionEntity);
					notificacionUsuarioEntity.setDestinatario(tablon.getProyecto().getCreador());

					notificacionRepository.save(notificacionEntity);
					notificacionUsuarioRepository.save(notificacionUsuarioEntity);
				}

			}
		}
		return "Orden actualizado";
	}

	@Transactional
	@Override
	public String eliminarTablon(ProyectoEntity proyecto, String nombreTablon, UsuarioEntity usuario) {

		// Primero: encontrar el tablon
		TablonEntity tablon = tablonRepository.findByNombreTablonAndProyecto(nombreTablon, proyecto);

		if (tablon == null) {
			return "Tablón no encontrado";
		}

		// Comprobamos que sigue existiendo en base de datos
		Optional<TablonEntity> tablonOpt = tablonRepository.findById(tablon.getId());
		if (tablonOpt.isEmpty()) {
			return "El tablón ya no existe";
		}

		// Segundo: recoger todas las tareas que estén en ese tablero
		List<TareaEntity> tareasAEliminar = new ArrayList<>();
		for (TareaEntity tarea : proyecto.getTareas()) {
			if (tarea.getEstado().equals(nombreTablon)) {
				tareasAEliminar.add(tarea);
			}
		}

		// Tercero: eliminar esas tareas de la base de datos
		tareaRepository.deleteAll(tareasAEliminar);
		proyecto.getTareas().removeAll(tareasAEliminar);

		// Cuarto: eliminar el tablon
		tablonRepository.delete(tablonOpt.get());
		tablonRepository.flush(); // sincronizamos ahora con la base de datos

		// SI EXISTE EL USUARIO QUE ELIMINO EL TABLON PUES CREAMOS NOTIFICACION
		if (usuario != null) {
			// CREAR NOTIFICACION PARA EL ADMINISTRADOR
			NotificacionEntity notificacionEntity = new NotificacionEntity();
			notificacionEntity.setProyecto(tablon.getProyecto());
			notificacionEntity.setTipo("ADMIN_MODIFICACION");
			notificacionEntity.setMensaje("El usuario " + usuario.getNombre() + " ha eliminado el tablon  " + nombreTablon);

			NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
			notificacionUsuarioEntity.setNotificacion(notificacionEntity);
			notificacionUsuarioEntity.setDestinatario(proyecto.getCreador());

			notificacionRepository.save(notificacionEntity);
			notificacionUsuarioRepository.save(notificacionUsuarioEntity);
		}

		return "Tablón eliminado exitosamente";
	}

	@Override
	public void guardarColores(UsuarioProyectoDTO usuarioProyectoDTO) {

		UsuarioProyectoEntity usuarioProyecto = usuarioProyectoRepository.findByUsuarioAndProyecto
		(
			usuarioRepository.findById(usuarioProyectoDTO.getIdUsuario()).orElse(null),
			proyectoRepository.findById(usuarioProyectoDTO.getIdProyecto()).orElse(null)
		);

		// SI ENCUENTRA EL PROYECTO DAR DE ALTA LOS COLORES
		if (usuarioProyecto != null) {
			if (usuarioProyectoDTO.getCustomColor() == null) {
				usuarioProyecto.setColorPrimario(usuarioProyectoDTO.getColor());
				usuarioProyecto.setColorHover(usuarioProyectoDTO.getColorHover());
			} else {
				usuarioProyecto.setCustomColor(usuarioProyectoDTO.getCustomColor());
			}
			usuarioProyectoRepository.save(usuarioProyecto);
		}

	}

	@Override
	public UsuarioProyectoDTO obtenerColores(Long usuarioId, Long proyectoId) {

		UsuarioProyectoEntity usuarioProyecto = usuarioProyectoRepository.findByUsuarioAndProyecto
		(
			usuarioRepository.findById(usuarioId).orElse(null),
			proyectoRepository.findById(proyectoId).orElse(null)
		);

		UsuarioProyectoDTO usuarioProyectoDTO = new UsuarioProyectoDTO();
		usuarioProyectoDTO.setColor(usuarioProyecto.getColorPrimario());
		usuarioProyectoDTO.setColorHover(usuarioProyecto.getColorHover());
		usuarioProyectoDTO.setCustomColor(usuarioProyecto.getCustomColor());
		return usuarioProyectoDTO;
	}

	@Override
	public boolean actualizarNombreTablon(Long idTablon, String nuevoNombre, UsuarioEntity usuario) {

		Optional<TablonEntity> optionalTablon = tablonRepository.findById(idTablon);

		if (optionalTablon.isPresent()) {
			TablonEntity tablon = optionalTablon.get();
			Long idProyecto = tablon.getProyecto().getIdProyecto();

			// Validar si ya existe otro tablón con ese nombre en el mismo proyecto
			boolean nombreExiste = tablonRepository.existsByNombreTablonIgnoreCaseAndIdNotAndProyecto_IdProyecto(nuevoNombre, idTablon, idProyecto);

			if (nombreExiste) {
				return false; // Ya existe otro tablón con ese nombre → no actualizamos
			}

			// SI EXISTE EL USUARIO QUE ACTUALIZO EL NOMBRE AL TABLON PUES CREAMOS
			// NOTIFICACION
			if (usuario != null) {
				// CREAR NOTIFICACION PARA EL ADMINISTRADOR
				NotificacionEntity notificacionEntity = new NotificacionEntity();
				notificacionEntity.setProyecto(tablon.getProyecto());
				notificacionEntity.setTipo("ADMIN_MODIFICACION");
				notificacionEntity.setMensaje("El usuario " + usuario.getNombre() + " ha actualizado el tablon  " + tablon.getNombreTablon() + " a " + nuevoNombre);

				NotificacionUsuarioEntity notificacionUsuarioEntity = new NotificacionUsuarioEntity();
				notificacionUsuarioEntity.setNotificacion(notificacionEntity);
				notificacionUsuarioEntity.setDestinatario(tablon.getProyecto().getCreador());

				notificacionRepository.save(notificacionEntity);
				notificacionUsuarioRepository.save(notificacionUsuarioEntity);
			}

			// No existe actualizamos
			List<TareaEntity> tareas = tareaService.obtenerTareasPorUserYProyecto(usuario.getIdUsuario(), idProyecto);

			// SI EL USUARIO TIENE TAREAS EN ESTE PROYECTO CAMBIAR SU ESTADO POR EL NUEVO
			// NOMBRE
			if (tareas.size() != 0) {
				for (int i = 0; i < tareas.size(); i++) {
					if (tareas.get(i).getEstado().equals(tablon.getNombreTablon())) {
						tareas.get(i).setEstado(nuevoNombre);
						tareaRepository.save(tareas.get(i));
					}
				}
			}

			tablon.setNombreTablon(nuevoNombre);
			tablonRepository.save(tablon);
			return true;

		}

		return false; // Tablón no encontrado
	}

}