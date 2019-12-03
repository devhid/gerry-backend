package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureEnumInterface;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
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

    // TODO: set app property for now
    @Getter
    private double epsilon;

    // TODO: change this to something like
    //  {
    //   compactness: { schwartzberg: 0.5 },
    //   fairness: { efficiency_gap: 0.8}
    //  }
    @Getter
    private Map<MeasureEnumInterface, Double> weights;

    @Getter
    private int numRetries;

    @Getter
    private PhaseTwoDepth depthHeuristic;

    @Getter
    private PhaseTwoPrecinctMove moveHeuristic;

}
