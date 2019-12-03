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

    private final AlgorithmSocketHandler algorithmSocketHandler;
    private final PrecinctSocketHandler precinctSocketHandler;

    @Autowired
    public WebsocketConfiguration(AlgorithmSocketHandler algorithmSocketHandler, PrecinctSocketHandler precinctSocketHandler) {
        this.algorithmSocketHandler = algorithmSocketHandler;
        this.precinctSocketHandler = precinctSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(algorithmSocketHandler, "/ws/algorithm/*").setAllowedOrigins("*");
        registry.addHandler(precinctSocketHandler, "/ws/precincts/*").setAllowedOrigins("*");
    }
}