package edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class IterativeAlgPhaseLog {

    protected String template;
    protected int iteration;

    public IterativeAlgPhaseLog(String template, int iteration) {
        this.template = template;
        this.iteration = iteration;
    }

    @Override
    @JsonValue
    public abstract String toString();

}
