package com.totaltasks.config;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totaltasks.models.ChatMessageDTO;
import com.totaltasks.services.ChatService;

@Component
public class ManejadorChatWebSocket extends TextWebSocketHandler {

	@Autowired
	private ChatService chatService;

	private final ObjectMapper mapper = new ObjectMapper();

	// Guarda las sesiones WebSocket activas por ID de proyecto
	private final Map<String, Set<WebSocketSession>> sesionesPorProyecto = new ConcurrentHashMap<>();

	// Cuando un usuario se conecta
	@Override
	public void afterConnectionEstablished(WebSocketSession sesion) {
		String idProyecto = obtenerIdProyectoDesdeSesion(sesion);

		// Añadir sesión al mapa del proyecto
		sesionesPorProyecto.computeIfAbsent(idProyecto, key -> ConcurrentHashMap.newKeySet()).add(sesion);

		// Enviar a todos los usuarios cuántos están conectados
		enviarUsuariosConectados(idProyecto);
	}

	// Cuando un usuario se desconecta
	@Override
	public void afterConnectionClosed(WebSocketSession sesion, CloseStatus estado) {
		String idProyecto = obtenerIdProyectoDesdeSesion(sesion);

		Set<WebSocketSession> sesiones = sesionesPorProyecto.get(idProyecto);
		if (sesiones != null) {
			sesiones.remove(sesion);
			if (sesiones.isEmpty()) {
				sesionesPorProyecto.remove(idProyecto);
			}
		}

		enviarUsuariosConectados(idProyecto);
	}

	// Cuando se recibe un mensaje desde el cliente
	@Override
	protected void handleTextMessage(WebSocketSession sesion, TextMessage mensajeTexto) throws IOException {
		// Convertir el JSON a objeto Java (DTO)
		ChatMessageDTO mensaje = mapper.readValue(mensajeTexto.getPayload(), ChatMessageDTO.class);

		// Establecer el ID del proyecto desde la URL
		String idProyecto = obtenerIdProyectoDesdeSesion(sesion);
		mensaje.setIdProyecto(Long.parseLong(idProyecto));

		// Guardar el mensaje en la base de datos
		ChatMessageDTO mensajeGuardado = chatService.guardarMensaje(mensaje);

		// Convertirlo nuevamente a JSON
		String mensajeJson = mapper.writeValueAsString(mensajeGuardado);

		// Enviar el mensaje a todos los usuarios conectados a ese proyecto
		for (WebSocketSession sesionDestino : sesionesPorProyecto.getOrDefault(idProyecto, Collections.emptySet())) {
			if (sesionDestino.isOpen()) {
				sesionDestino.sendMessage(new TextMessage(mensajeJson));
			}
		}
	}

	// Envía el número de usuarios conectados al proyecto
	private void enviarUsuariosConectados(String idProyecto) {
		int cantidad = sesionesPorProyecto.getOrDefault(idProyecto, Collections.emptySet()).size();
		String mensaje = "{\"type\":\"count\", \"data\":" + cantidad + "}";

		for (WebSocketSession sesion : sesionesPorProyecto.getOrDefault(idProyecto, Collections.emptySet())) {
			if (sesion.isOpen()) {
				try {
					sesion.sendMessage(new TextMessage(mensaje));
				} catch (IOException e) {
					e.printStackTrace(); // Solo loguea error, no interrumpe
				}
			}
		}
	}

	// Obtiene el ID del proyecto desde la URL del WebSocket
	private String obtenerIdProyectoDesdeSesion(WebSocketSession sesion) {
		String url = sesion.getUri().getPath();
		return url.substring(url.lastIndexOf('/') + 1);
	}
}