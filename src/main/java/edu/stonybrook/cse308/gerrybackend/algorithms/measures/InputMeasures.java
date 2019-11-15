package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public abstract class InputMeasures {

    static double computeScore(MeasureEnumInterface measure, StateNode state){
        if (measure instanceof CompactnessEnum){
            return CompactnessMeasure.computeCompactnessScore((CompactnessEnum) measure, state);
        }
        else if (measure instanceof PoliticalCompetitivenessEnum){
            return PoliticalCompetitivenessMeasure.computeCompetitivenessScore((PoliticalCompetitivenessEnum) measure, state);
        }
        else if (measure instanceof PoliticalFairnessEnum){
            return PoliticalFairnessMeasure.computeFairnessScore((PoliticalFairnessEnum) measure, state);
        }
        else if (measure instanceof PopulationEqualityEnum){
            return PopulationEqualityMeasure.computePopulationEqualityScore((PopulationEqualityEnum) measure, state);
        }
        else if (measure instanceof PopulationHomogeneityEnum){
            return PopulationHomogeneityMeasure.computePopulationHomogeneityScore((PopulationHomogeneityEnum) measure, state);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

}
