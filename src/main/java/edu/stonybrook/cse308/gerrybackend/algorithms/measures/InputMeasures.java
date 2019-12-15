package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public abstract class InputMeasures {

    public static double computeScore(MeasureInterface measureInterface, DistrictNode district) {
        if (measureInterface instanceof Compactness) {
            return CompactnessMeasure.computeCompactnessScore((Compactness) measureInterface, district);
        } else if (measureInterface instanceof PoliticalCompetitiveness) {
            return PoliticalCompetitivenessMeasure.computeCompetitivenessScore((PoliticalCompetitiveness) measureInterface, district);
        } else if (measureInterface instanceof PoliticalFairness) {
            return PoliticalFairnessMeasure.computeFairnessScore((PoliticalFairness) measureInterface, district);
        } else if (measureInterface instanceof PopulationEquality) {
            return PopulationEqualityMeasure.computePopulationEqualityScore((PopulationEquality) measureInterface, district);
        } else if (measureInterface instanceof PopulationHomogeneity) {
            return PopulationHomogeneityMeasure.computePopulationHomogeneityScore((PopulationHomogeneity) measureInterface, district);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

    public static double computeScore(MeasureInterface measureInterface, StateNode state) {
        if (measureInterface instanceof PopulationEquality) {
            return PopulationEqualityMeasure.computePopulationEqualityScore((PopulationEquality) measureInterface, state);
        } else if (measureInterface instanceof PoliticalFairness) {
            return PoliticalFairnessMeasure.computeFairnessScore((PoliticalFairness) measureInterface, state);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

}
