package edu.stonybrook.cse308.gerrybackend.config;

import edu.stonybrook.cse308.gerrybackend.controllers.sockets.AlgorithmSocketHandler;
import edu.stonybrook.cse308.gerrybackend.controllers.sockets.PrecinctSocketHandler;
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
    @Autowired
    PrecinctSocketHandler precinctSocketHandler;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(algorithmSocketHandler, "/ws/algorithm/*").setAllowedOrigins("*");
        registry.addHandler(precinctSocketHandler, "/ws/precincts/*").setAllowedOrigins("*");
    }
}