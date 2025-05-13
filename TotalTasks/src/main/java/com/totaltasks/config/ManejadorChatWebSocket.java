package com.totaltasks.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ManejadorChatWebSocket extends TextWebSocketHandler {

    // Mapa: idProyecto → sesiones de ese proyecto
    private final Map<String, Set<WebSocketSession>> sesionesPorProyecto = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String idProyecto = extraerIdProyecto(session);
        sesionesPorProyecto.computeIfAbsent(idProyecto, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String idProyecto = extraerIdProyecto(session);
        Set<WebSocketSession> set = sesionesPorProyecto.get(idProyecto);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) {
                sesionesPorProyecto.remove(idProyecto);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String idProyecto = extraerIdProyecto(session);
        Set<WebSocketSession> set = sesionesPorProyecto.getOrDefault(idProyecto, Collections.emptySet());
        for (WebSocketSession s : set) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }

	// Envía a todos la cantidad de usuarios conectados
	// private void enviarCantidadUsuarios() throws Exception {
	// 	String mensajeCantidad = "{\"type\":\"count\", \"data\":" + sesionesConectadas.size() + "}";
	// 	for (WebSocketSession sesion : sesionesConectadas) {
	// 		if (sesion.isOpen()) {
	// 			sesion.sendMessage(new TextMessage(mensajeCantidad));
	// 		}
	// 	}
	// }

    // Helper: obtiene el {idProyecto} de la URI "/chat/{idProyecto}"
    private String extraerIdProyecto(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}