package com.totaltasks.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ManejadorChatWebSocket extends TextWebSocketHandler {

	// Conjunto de sesiones conectadas
	private final Set<WebSocketSession> sesionesConectadas = ConcurrentHashMap.newKeySet();

	// Se ejecuta cuando un usuario se conecta
	@Override
	public void afterConnectionEstablished(WebSocketSession sesion) throws Exception {
		sesionesConectadas.add(sesion);
		enviarCantidadUsuarios();
	}

	// Se ejecuta cuando un usuario se desconecta
	@Override
	public void afterConnectionClosed(WebSocketSession sesion, CloseStatus estado) throws Exception {
		sesionesConectadas.remove(sesion);
		enviarCantidadUsuarios();
	}

	// Se ejecuta cuando llega un mensaje de un usuario
	@Override
	protected void handleTextMessage(WebSocketSession sesion, TextMessage mensaje) throws Exception {
		for (WebSocketSession s : sesionesConectadas) {
			if (s.isOpen()) {
				s.sendMessage(mensaje);
			}
		}
	}

	// Envía a todos la cantidad de usuarios conectados
	private void enviarCantidadUsuarios() throws Exception {
		String mensajeCantidad = "{\"type\":\"count\", \"data\":" + sesionesConectadas.size() + "}";
		for (WebSocketSession sesion : sesionesConectadas) {
			if (sesion.isOpen()) {
				sesion.sendMessage(new TextMessage(mensajeCantidad));
			}
		}
	}
	
}