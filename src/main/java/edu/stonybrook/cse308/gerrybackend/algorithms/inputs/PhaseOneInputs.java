package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import lombok.Getter;

import java.util.Set;

public class PhaseOneInputs extends AlgPhaseInputs {

    @Getter
    private int numDistricts;

    @Getter
    private Set<DemographicType> demographicTypes;

    @Getter
    private double upperBound;

    @Getter
    private double lowerBound;

    @Getter
    private PhaseOneStop stopHeuristic;

    @Getter
    private PhaseOneMajMinPairs phaseOneMajMinPairs;

}
