package com.totaltasks.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totaltasks.models.ChatMessageDTO;
import com.totaltasks.services.ChatService;

@Component
public class ManejadorChatWebSocket extends TextWebSocketHandler {

	@Autowired
	private ChatService chatService;

	private final ObjectMapper mapper = new ObjectMapper();

	private final Map<String, Set<WebSocketSession>> sesionesPorProyecto = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String idProj = extraerIdProyecto(session);
		sesionesPorProyecto
				.computeIfAbsent(idProj, k -> ConcurrentHashMap.newKeySet())
				.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		String idProj = extraerIdProyecto(session);
		Set<WebSocketSession> set = sesionesPorProyecto.get(idProj);
		if (set != null) {
			set.remove(session);
			if (set.isEmpty()) {
				sesionesPorProyecto.remove(idProj);
			}
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 1) Parsear payload → DTO
		ChatMessageDTO dto = mapper.readValue(message.getPayload(), ChatMessageDTO.class);
		// 2) Fijar proyecto según URI
		dto.setIdProyecto(Long.parseLong(extraerIdProyecto(session)));
		// 3) Guardar en BD (service asigna usuario, timestamp, etc.)
		ChatMessageDTO guardado = chatService.guardarMensaje(dto);
		// 4) Serializar el DTO con todos los campos (incluye id, fechaCreacion)
		String broadcastPayload = mapper.writeValueAsString(guardado);
		// 5) Reenviar a todas las sesiones de ese proyecto
		String idProj = extraerIdProyecto(session);
		for (WebSocketSession s : sesionesPorProyecto.getOrDefault(idProj, Collections.emptySet())) {
			if (s.isOpen()) {
				s.sendMessage(new TextMessage(broadcastPayload));
			}
		}
	}

	// // Envía a todos la cantidad de usuarios conectados
    // private void enviarCantidadUsuarios(String idProyecto) throws IOException {

    //     Set<WebSocketSession> sesiones = sesionesPorProyecto.getOrDefault(idProyecto, Collections.emptySet());
    //     int cantidad = sesiones.size();
    //     String mensaje = "{\"type\":\"count\", \"data\":" + cantidad + "}";
    //     for (WebSocketSession sesion : sesiones) {
    //         if (sesion.isOpen()) {
    //             sesion.sendMessage(new TextMessage(mensaje));
    //         }
    //     }

    // }

	private String extraerIdProyecto(WebSocketSession session) {
		String path = session.getUri().getPath();
		return path.substring(path.lastIndexOf('/') + 1);
	}
}