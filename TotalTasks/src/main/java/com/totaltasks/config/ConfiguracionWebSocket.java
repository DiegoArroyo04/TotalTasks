package com.totaltasks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class ConfiguracionWebSocket implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ManejadorChatWebSocket(), "/chat/{idProyecto}")
                .setAllowedOrigins("*");
    }
}