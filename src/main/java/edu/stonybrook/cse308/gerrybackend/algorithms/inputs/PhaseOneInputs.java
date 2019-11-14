package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStopEnum;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;

import java.util.Set;

public class PhaseOneInputs extends AlgPhaseInputs {

    private int numDistricts;
    private Set<DemographicType> demographicTypes;
    private double upperBound;
    private double lowerBound;
    private PhaseOneStopEnum stopHeuristic;
}
