package edu.stonybrook.cse308.gerrybackend.config;

import edu.stonybrook.cse308.gerrybackend.controllers.AlgorithmSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    // Autowire our websocket handlers
    @Autowired
    AlgorithmSocketHandler algorithmSocketHandler;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {

        registry.addHandler(algorithmSocketHandler, "/algorithm").setAllowedOrigins("*");
    }
}