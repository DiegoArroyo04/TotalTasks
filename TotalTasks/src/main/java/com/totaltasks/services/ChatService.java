package com.totaltasks.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ChatMessageEntity;
import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ChatMessageDTO;
import com.totaltasks.repositories.ChatMessageRepository;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.UsuarioRepository;

@Service
public class ChatService {

	@Autowired
	private ChatMessageRepository chatRepo;

	@Autowired
	private ProyectoRepository proyectoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	// Guarda el mensaje y retorna el DTO completo (con id y fechaCreacion).
	public ChatMessageDTO guardarMensaje(ChatMessageDTO dto) {
		ChatMessageEntity ent = new ChatMessageEntity();
		ent.setContenido(dto.getContenido());

		ProyectoEntity proyecto = proyectoRepo.findById(dto.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario()).orElse(null);
		ent.setProyecto(proyecto);
		ent.setUsuario(usuario);

		ChatMessageEntity saved = chatRepo.save(ent);

		// Construir DTO de respuesta
		ChatMessageDTO out = new ChatMessageDTO();
		out.setId_message(saved.getId_message());
		out.setContenido(saved.getContenido());
		out.setFechaCreacion(saved.getFechaCreacion());
		out.setIdProyecto(saved.getProyecto().getIdProyecto());
		out.setIdUsuario(saved.getUsuario().getIdUsuario());
		return out;
	}

	// Devuelve el historial ordenado por fecha ascendente.
	public List<ChatMessageDTO> obtenerMensajesPorProyecto(Long idProyecto) {
		List<ChatMessageDTO> lista = new ArrayList<>();
		for (ChatMessageEntity ent : chatRepo.findByProyecto_IdProyectoOrderByFechaCreacionAsc(idProyecto)) {
			ChatMessageDTO dto = new ChatMessageDTO();
			dto.setId_message(ent.getId_message());
			dto.setContenido(ent.getContenido());
			dto.setFechaCreacion(ent.getFechaCreacion());
			dto.setIdProyecto(ent.getProyecto().getIdProyecto());
			dto.setIdUsuario(ent.getUsuario().getIdUsuario());
			lista.add(dto);
		}
		return lista;
	}
}