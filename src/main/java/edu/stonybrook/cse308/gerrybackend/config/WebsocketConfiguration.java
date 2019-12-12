package edu.stonybrook.cse308.gerrybackend.config;

import edu.stonybrook.cse308.gerrybackend.controllers.sockets.PhaseOneSocketHandler;
import edu.stonybrook.cse308.gerrybackend.controllers.sockets.PrecinctSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    private final PhaseOneSocketHandler phaseOneSocketHandler;
    private final PrecinctSocketHandler precinctSocketHandler;

    @Autowired
    public WebsocketConfiguration(PhaseOneSocketHandler phaseOneSocketHandler, PrecinctSocketHandler precinctSocketHandler) {
        this.phaseOneSocketHandler = phaseOneSocketHandler;
        this.precinctSocketHandler = precinctSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(phaseOneSocketHandler, "/ws/algorithm/phase1/*").setAllowedOrigins("*");
        registry.addHandler(precinctSocketHandler, "/ws/precincts/*").setAllowedOrigins("*");
    }
}