package edu.stonybrook.cse308.gerrybackend.config;

import edu.stonybrook.cse308.gerrybackend.controllers.sockets.PhaseOneSocketHandler;
import edu.stonybrook.cse308.gerrybackend.controllers.sockets.PrecinctSocketHandler;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import javax.persistence.EntityManagerFactory;

@Configuration
//@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

//    private final PhaseOneSocketHandler phaseOneSocketHandler;
//    private final PrecinctSocketHandler precinctSocketHandler;
//
//    @Autowired
//    public WebsocketConfiguration(PhaseOneSocketHandler phaseOneSocketHandler, PrecinctSocketHandler precinctSocketHandler) {
//        this.phaseOneSocketHandler = phaseOneSocketHandler;
//        this.precinctSocketHandler = precinctSocketHandler;
//    }

//    @Override
//    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
//        registry.addHandler(phaseOneSocketHandler, "/ws/algorithm/phase1/*").setAllowedOrigins("*");
//        registry.addHandler(precinctSocketHandler, "/ws/precincts/*").setAllowedOrigins("*");
//    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
    }

}