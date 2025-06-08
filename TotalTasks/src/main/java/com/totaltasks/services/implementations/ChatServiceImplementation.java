package com.totaltasks.services.implementations;

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
import com.totaltasks.services.ChatService;

@Service
public class ChatServiceImplementation implements ChatService {

	@Autowired
	private ChatMessageRepository chatRepo;

	@Autowired
	private ProyectoRepository proyectoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Override
	public ChatMessageDTO guardarMensaje(ChatMessageDTO dto) {

		ProyectoEntity proyecto = proyectoRepo.findById(dto.getIdProyecto()).orElse(null);
		UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario()).orElse(null);

		ChatMessageEntity ent = new ChatMessageEntity();

		ent.setContenido(dto.getContenido());
		ent.setProyecto(proyecto);
		ent.setUsuario(usuario);

		ChatMessageEntity saved = chatRepo.save(ent);

		ChatMessageDTO out = new ChatMessageDTO();

		out.setId_message(saved.getId_message());
		out.setContenido(saved.getContenido());
		out.setFechaCreacion(saved.getFechaCreacion());
		out.setIdProyecto(saved.getProyecto().getIdProyecto());
		out.setIdUsuario(saved.getUsuario().getIdUsuario());
		out.setNombreUsuario(usuario.getUsuario());

		return out;
	}

	@Override
	public List<ChatMessageDTO> obtenerMensajesPorProyecto(Long idProyecto) {

		List<ChatMessageDTO> lista = new ArrayList<>();

		for (ChatMessageEntity ent : chatRepo.findByProyecto_IdProyectoOrderByFechaCreacionAsc(idProyecto)) {

			ChatMessageDTO dto = new ChatMessageDTO();

			dto.setId_message(ent.getId_message());
			dto.setContenido(ent.getContenido());
			dto.setFechaCreacion(ent.getFechaCreacion());
			dto.setIdProyecto(ent.getProyecto().getIdProyecto());
			dto.setIdUsuario(ent.getUsuario().getIdUsuario());
			dto.setNombreUsuario(ent.getUsuario().getUsuario());
			lista.add(dto);
		}

		return lista;
	}
}