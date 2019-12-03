package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import lombok.Getter;

public class PhaseZeroInputs extends AlgPhaseInputs {

    @Getter
    private double populationThreshold;

    @Getter
    private double voteThreshold;

    @Override
    protected boolean isValid() {
        return super.isValid();
    }
}
