package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.repositories.NotificacionRepository;
import com.totaltasks.repositories.NotificacionUsuarioRepository;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.TablonRepository;
import com.totaltasks.repositories.TareaRepository;
import com.totaltasks.repositories.UsuarioProyectoRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;

import jakarta.transaction.Transactional;

@Service
public class ProyectoServiceImplementation implements ProyectoService {

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TablonRepository tablonRepository;

	@Autowired
	private TareaRepository tareaRepository;

	@Autowired
	private UsuarioProyectoRepository usuarioProyectoRepository;

	@Autowired
	private NotificacionRepository notificacionRepository;

	@Autowired
	private NotificacionUsuarioRepository notificacionUsuarioRepository;

	@Autowired
	private TablonService tablonService;

	@Override
	public List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario) {
		return proyectoRepository.findTodosLosProyectosDeUnUsuario(usuario.getIdUsuario());
	}

	@Override
	public void guardarProyecto(ProyectoDTO proyectoDTO) {

		// Guardamos el proyecto
		ProyectoEntity proyectoEntity = new ProyectoEntity();

		proyectoEntity.setNombreProyecto(proyectoDTO.getNombreProyecto());
		proyectoEntity.setDescripcion(proyectoDTO.getDescripcion());
		proyectoEntity.setMetodologia(proyectoDTO.getMetodologia());
		proyectoEntity.setCodigo(obtenerCodigoAleatorio());

		UsuarioEntity creador = usuarioRepository.findById(proyectoDTO.getIdCreador()).orElse(null);
		proyectoEntity.setCreador(creador);

		proyectoRepository.save(proyectoEntity);

		// Guardamos la relacion entre el Usuario y el Proyecto
		UsuarioProyectoEntity usuarioProyecto = new UsuarioProyectoEntity();

		usuarioProyecto.setUsuario(creador);
		usuarioProyecto.setProyecto(proyectoEntity);
		usuarioProyecto.setRol("Administrador");

		usuarioProyectoRepository.save(usuarioProyecto);

		// CREAR TABLONES POR DEFECTO
		if (proyectoEntity.getMetodologia().equals("Kanban")) {
			TablonDTO tablonPorHacer = new TablonDTO("Por Hacer", 1, proyectoEntity);
			TablonDTO tablonEnCurso = new TablonDTO("En Curso", 2, proyectoEntity);
			TablonDTO tablonHecho = new TablonDTO("Hecho", 3, proyectoEntity);

			tablonService.crearTablon(tablonPorHacer, null);
			tablonService.crearTablon(tablonEnCurso, null);
			tablonService.crearTablon(tablonHecho, null);
		} else if (proyectoEntity.getMetodologia().equals("Scrum")) {
			TablonDTO tablonBacklog = new TablonDTO("Backlog", 2, proyectoEntity);

			tablonService.crearTablon(tablonBacklog, null);
		}
	}

	@Override
	public boolean unirseAProyectoPorCodigo(String codigo, UsuarioEntity usuario) {
		ProyectoEntity proyecto = proyectoRepository.findByCodigo(codigo);

		if (proyecto == null) {
			return false;
		}

		// Comprobar si ya está unido
		boolean yaUnido = usuarioProyectoRepository.existsByUsuarioAndProyecto(usuario, proyecto);
		if (yaUnido) {
			return false;
		}

		UsuarioProyectoEntity usuarioProyecto = new UsuarioProyectoEntity();
		usuarioProyecto.setUsuario(usuario);
		usuarioProyecto.setProyecto(proyecto);
		usuarioProyecto.setRol("Colaborador");

		usuarioProyectoRepository.save(usuarioProyecto);
		return true;
	}

	@Override
	public ProyectoEntity obtenerProyectoPorNombreYUsuario(String nombreProyecto, UsuarioEntity usuarioEntity) {
		// RECUPERAR TODOS LOS PROYECTOS QUE TENGAN ESE NOMBRE
		List<ProyectoEntity> proyectos = proyectoRepository.findByNombreProyecto(nombreProyecto);
		ProyectoEntity proyectoADevolver = null;
		if (proyectos.isEmpty()) {
			return null;
		} else {
			// Buscar en cada proyecto si contiene al usuario
			for (ProyectoEntity proyecto : proyectos) {
				for (UsuarioProyectoEntity usuario : proyecto.getUsuarios()) {
					if (usuario.getUsuario().getEmail().equals(usuarioEntity.getEmail())) {
						proyectoADevolver = proyecto;
					}
				}
			}
		}
		return proyectoADevolver;

	}

	@Override
	public ProyectoEntity obtenerProyectoPorId(Long id) {
		return proyectoRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteById(Long id, boolean abandonar) {
		ProyectoEntity proyecto = obtenerProyectoPorId(id);

		if (abandonar == true) {
			usuarioProyectoRepository.deleteAllByProyecto(proyecto);
		} else {
			notificacionRepository.deleteAllByProyecto(proyecto);
			notificacionUsuarioRepository.deleteAllByProyectoId(proyecto.getIdProyecto());

			tareaRepository.deleteAllByProyecto(proyecto);
			tablonRepository.deleteAllByProyecto(proyecto);
			usuarioProyectoRepository.deleteAllByProyecto(proyecto);
			proyectoRepository.deleteById(id);
		}

	}

	@Override
	public ProyectoEntity findByCodigo(String codigo) {
		return proyectoRepository.findByCodigo(codigo);
	}

	@Override
	public String obtenerCodigoAleatorio() {

		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String codigo = "";

		do {
			codigo = "";
			for (int i = 0; i < 10; i++) {
				int index = (int) (Math.random() * caracteres.length());
				codigo += caracteres.charAt(index);
			}

		} while (proyectoRepository.findByCodigo(codigo) != null);

		return codigo;

	}

	@Override
	public RepoDTO comprobarRepo(ProyectoEntity proyecto, List<RepoDTO> repositorios) {

		RepoDTO repoEncontrado = null;

		// COMPROBAR QUE EL PROYECTO TENGA UN REPO
		for (int i = 0; i < repositorios.size(); i++) {
			if (repositorios.get(i).getName().equals(proyecto.getNombreProyecto())) {
				repoEncontrado = repositorios.get(i);
			}
		}

		return repoEncontrado;
	}

	@Override
	public boolean usuarioExiste(ProyectoEntity proyecto, UsuarioEntity usuario) {
		boolean encontrado = false;
		// COMPROBAR QUE UN USUARIO EXISTA EN UN PROYECTO
		for (int i = 0; i < proyecto.getUsuarios().size(); i++) {
			if (usuario.getEmail().equals(proyecto.getUsuarios().get(i).getUsuario().getEmail())) {
				encontrado = true;
			}
		}

		return encontrado;
	}

}