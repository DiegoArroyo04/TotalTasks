package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.TablonRepository;
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
	private UsuarioProyectoRepository usuarioProyectoRepository;

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

			tablonService.crearTablon(tablonPorHacer);
			tablonService.crearTablon(tablonEnCurso);
			tablonService.crearTablon(tablonHecho);

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
	public ProyectoEntity obtenerProyectoPorNombre(String nombreProyecto) {
		List<ProyectoEntity> proyectos = proyectoRepository.findByNombreProyecto(nombreProyecto);
		return proyectos.isEmpty() ? null : proyectos.get(0);
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

}