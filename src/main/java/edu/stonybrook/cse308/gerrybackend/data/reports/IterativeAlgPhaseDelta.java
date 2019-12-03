package edu.stonybrook.cse308.gerrybackend.data.reports;

import lombok.Getter;

public abstract class IterativeAlgPhaseDelta {

    @Getter
    public int iteration;

    public IterativeAlgPhaseDelta(int iteration) {
        this.iteration = iteration;
    }

}
