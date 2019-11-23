package edu.stonybrook.cse308.gerrybackend.controllers.sockets;

import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.PrecinctSocketBuffer;
import edu.stonybrook.cse308.gerrybackend.utils.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class PrecinctSocketHandler extends TextWebSocketHandler {

    private final int BATCH_SIZE = 100;

    @Autowired
    private StateRepository stateRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PrecinctSocketHandler.class);

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOGGER.info(String.format("Connected to session %s.", session.getId()));
        final String state = UriUtils.getStatePath(session.getUri());
        final Optional<StateNode> stateNode = stateRepository.findById(state);
        CloseStatus closeStatus = new CloseStatus(CloseStatus.NORMAL.getCode(), "Completed fetching precincts.");

        if (stateNode.isEmpty()) {
            closeStatus = new CloseStatus(CloseStatus.SERVER_ERROR.getCode(), "Invalid state selected.");
            session.close(closeStatus);
            return;
        }

        final Set<PrecinctNode> precincts = stateNode.get().getAllPrecincts();
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<?> future = executorService.submit(new PrecinctSocketBuffer(session, precincts, BATCH_SIZE));

        try {
            future.get();
        } catch (final ExecutionException e) {
            closeStatus = new CloseStatus(CloseStatus.SERVER_ERROR.getCode(), "Error transmitting precincts.");
        } finally {
            session.close(closeStatus);
        }
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
