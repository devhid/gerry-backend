package edu.stonybrook.cse308.gerrybackend.utils;

import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.concurrent.Callable;

public class PrecinctSocketBuffer implements Callable<Void> {

    @Getter
    private final WebSocketSession session;
    private final List<String> collection;
    private final int batchSize;

    public PrecinctSocketBuffer(final WebSocketSession session, final List<String> collection, final int batchSize) {
        this.session = session;
        this.collection = collection;
        this.batchSize = batchSize;
    }

    @Override
    public Void call() throws Exception {
        for (int i = 0; i < this.collection.size(); i += Math.min(this.batchSize, this.collection.size() - i + 1)) {
            List<String> messages = this.collection.subList(i, Math.min(this.batchSize + i, this.collection.size() - i));
            this.session.sendMessage(new TextMessage(messages.toString()));
        }
        return null;
    }

}
