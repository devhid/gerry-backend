package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationEqualityEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PopulationEqualityMeasure {

    class MostToLeast {
        public static double computePopulationEqualityScore(StateNode state) {
            DistrictNode mostPopulated = getMostPopulatedDistrict(state);
            DistrictNode leastPopulated = getLeastPopulatedDistrict(state);
            return computePercentDifference(mostPopulated, leastPopulated);
        }

        private static DistrictNode getExtremePopulationDistrict(StateNode state, boolean mostPopulated) {
            DistrictNode extremeDistrict = null;
            for (DistrictNode d : state.getChildren()) {
                int currentMostPop = (extremeDistrict == null) ? 0 : extremeDistrict.getDemographicData().getTotalPopulation();
                int districtPop = d.getDemographicData().getTotalPopulation();
                if (mostPopulated) {
                    extremeDistrict = (districtPop > currentMostPop) ? d : extremeDistrict;
                } else {
                    extremeDistrict = (districtPop < currentMostPop) ? d : extremeDistrict;
                }
            }
            return extremeDistrict;
        }

        private static DistrictNode getMostPopulatedDistrict(StateNode state) {
            return MostToLeast.getExtremePopulationDistrict(state, true);
        }

        private static DistrictNode getLeastPopulatedDistrict(StateNode state) {
            return MostToLeast.getExtremePopulationDistrict(state, false);
        }

        private static double computePercentDifference(DistrictNode mostPopulated, DistrictNode leastPopulated) {
            int maxPopulation = mostPopulated.getDemographicData().getTotalPopulation();
            int leastPopulation = leastPopulated.getDemographicData().getTotalPopulation();
            int difference = maxPopulation - leastPopulation;
            double avg = ((double) (maxPopulation + leastPopulation)) / 2;
            return difference / avg;
        }
    }

    class Ideal {
        public static double computePopulationEqualityScore(DistrictNode district) {
            StateNode state = district.getParent();
            double idealPopulation = (double) state.getDemographicData().getTotalPopulation() / state.getChildren().size();
            int truePopulation = district.getDemographicData().getTotalPopulation();
            if (idealPopulation >= truePopulation) {
                return ((double) truePopulation) / idealPopulation;
            }
            return idealPopulation / truePopulation;
        }
    }

    static double computePopulationEqualityScore(PopulationEqualityEnum measure, DistrictNode district) {
        double popEqualityScore;
        switch (measure) {
            case IDEAL:
                popEqualityScore = Ideal.computePopulationEqualityScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return popEqualityScore;
    }

    static double computePopulationEqualityScore(PopulationEqualityEnum measure, StateNode state) {
        double popEqualityScore;
        switch (measure) {
            case MOST_TO_LEAST:
                popEqualityScore = MostToLeast.computePopulationEqualityScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return popEqualityScore;
    }

}
