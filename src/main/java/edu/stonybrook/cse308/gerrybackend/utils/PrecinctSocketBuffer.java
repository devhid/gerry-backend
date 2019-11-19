package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PrecinctSocketBuffer implements Callable<Void> {

    @Getter
    private final WebSocketSession session;
    private final List<PrecinctNode> collection;
    private final int batchSize;

    public PrecinctSocketBuffer(final WebSocketSession session, final Set<PrecinctNode> collection, final int batchSize) {
        this.session = session;
        this.collection = new ArrayList<>(collection);
        this.batchSize = batchSize;
    }

    @Override
    public Void call() throws Exception {
        for (int i = 0; i < this.collection.size(); i += Math.min(this.batchSize, this.collection.size() - i + 1)) {
            List<PrecinctNode> precincts = this.collection.subList(i, Math.min(this.batchSize + i, this.collection.size() - i));
            List<String> precinctJson = precincts.stream()
                    .map(precinct -> precinct.getGeography())
                    .collect(Collectors.toList());
            this.session.sendMessage(new TextMessage(precinctJson.toString()));
        }
        return null;
    }

}
