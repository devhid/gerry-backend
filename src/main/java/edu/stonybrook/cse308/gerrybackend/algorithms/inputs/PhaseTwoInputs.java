package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.types.AlgRunType;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import lombok.Getter;

import java.util.Set;

public class PhaseTwoInputs extends AlgPhaseInputs {

    private Set<DemographicType> demographicTypes;
    private double upperBound;
    private double lowerBound;
    private double epsilon;
    private PhaseTwoDepth phaseTwoDepth;
    @Getter
    protected AlgRunType algRunType;
}
