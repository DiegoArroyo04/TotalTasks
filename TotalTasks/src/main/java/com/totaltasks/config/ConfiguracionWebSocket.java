package com.totaltasks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class ConfiguracionWebSocket implements WebSocketConfigurer {

	@Autowired
	private ManejadorChatWebSocket manejador;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// Registrar el WebSocket en la ruta
		registry.addHandler(manejador, "/chat/{idProyecto}").setAllowedOrigins("*");
	}
}