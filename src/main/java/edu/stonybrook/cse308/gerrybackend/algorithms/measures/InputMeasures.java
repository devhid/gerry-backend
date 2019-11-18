package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public abstract class InputMeasures {

    static double computeScore(MeasureEnumInterface measure, DistrictNode district){
        if (measure instanceof CompactnessEnum){
            return CompactnessMeasure.computeCompactnessScore((CompactnessEnum) measure, district);
        }
        else if (measure instanceof PoliticalCompetitivenessEnum){
            return PoliticalCompetitivenessMeasure.computeCompetitivenessScore((PoliticalCompetitivenessEnum) measure, district);
        }
        else if (measure instanceof PoliticalFairnessEnum){
            return PoliticalFairnessMeasure.computeFairnessScore((PoliticalFairnessEnum) measure, district);
        }
        else if (measure instanceof PopulationEqualityEnum){
            return PopulationEqualityMeasure.computePopulationEqualityScore((PopulationEqualityEnum) measure, district);
        }
        else if (measure instanceof PopulationHomogeneityEnum){
            return PopulationHomogeneityMeasure.computePopulationHomogeneityScore((PopulationHomogeneityEnum) measure, district);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

    static double computeScore(MeasureEnumInterface measure, StateNode state){
        if (measure instanceof PopulationEqualityEnum){
            return PopulationEqualityMeasure.computePopulationEqualityScore((PopulationEqualityEnum) measure, state);
        }
        else if (measure instanceof PoliticalFairnessEnum){
            return PoliticalFairnessMeasure.computeFairnessScore((PoliticalFairnessEnum) measure, state);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

}
