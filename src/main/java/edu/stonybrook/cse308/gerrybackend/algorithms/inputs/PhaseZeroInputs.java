package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import lombok.Getter;

import java.util.Set;

public class PhaseZeroInputs extends AlgPhaseInputs {

    @Getter
    private double populationThreshold;

    @Getter
    private double voteThreshold;

}
