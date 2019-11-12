package edu.stonybrook.cse308.gerrybackend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class AlgorithmSocketHandler extends TextWebSocketHandler {

    final static Logger LOGGER = LoggerFactory.getLogger(AlgorithmSocketHandler.class);

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOGGER.info(String.format("Connected to session %s.", session.getId()));
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage json) throws Exception {
        LOGGER.info(String.format("Received message: %s", json.getPayload()));
    }

    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable throwable) throws Exception {
        LOGGER.error(String.format("Error from sender %s", session.toString()), throwable);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        LOGGER.info(String.format("Session %s closed because of %s.", session.getId(), status.getReason()));
    }

}
