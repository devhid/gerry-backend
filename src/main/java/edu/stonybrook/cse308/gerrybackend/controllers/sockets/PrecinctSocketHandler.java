package edu.stonybrook.cse308.gerrybackend.controllers.sockets;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;

@Component
public class PrecinctSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrecinctSocketHandler.class);

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOGGER.info(String.format("Connected to session %s.", session.getId()));
        // TODO: Immediately send precinct data upon connection?
        // TODO: Extract the 'geography' field from the object since that contains well-formed JSON for FE
        final String state = UriUtils.getLastPath(session.getUri());
        Set<PrecinctNode> precincts = null;
        if (state.equals(StateType.CALIFORNIA.getName())) {
            LOGGER.info("CA");
        } else if (state.equals(StateType.UTAH.getName())) {
            LOGGER.info("UT");
        } else if (state.equals(StateType.VIRGINIA.getName())) {
            LOGGER.info("VA");
        } else {
            LOGGER.error("Invalid state detected.");
        }
        session.close(new CloseStatus(CloseStatus.NORMAL.getCode(), "Completed fetching precincts"));
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
