package edu.stonybrook.cse308.gerrybackend.communication;

import lombok.Getter;
import lombok.Setter;

public class WebSocketPing {

    @Getter
    @Setter
    private String action;

    @Getter
    @Setter
    private String value;

    public WebSocketPing(final String action, final String value) {
        this.action = action;
        this.value = value;
    }

}
