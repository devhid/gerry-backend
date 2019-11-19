package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureEnumInterface;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgRunType;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

public class PhaseTwoInputs extends AlgPhaseInputs {

    @Getter
    private String stateId;

    @Getter
    private Set<DemographicType> demographicTypes;

    @Getter
    private double upperBound;

    @Getter
    private double lowerBound;

    @Getter
    private double epsilon;

    @Getter
    private Map<MeasureEnumInterface,Double> weights;

    @Getter
    private PhaseTwoDepth phaseTwoDepthHeuristic;

    @Getter
    private PhaseTwoPrecinctMove precinctMoveHeuristic;

    @Getter
    private AlgRunType algRunType;

}
