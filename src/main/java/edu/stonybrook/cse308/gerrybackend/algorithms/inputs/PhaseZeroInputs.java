package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;

import java.util.Set;

public class PhaseZeroInputs extends AlgPhaseInputs {

    private Set<DemographicType> demographicTypes;
    private ElectionType electionType;
    private double populationThreshold;
    private double voteThreshold;

}
