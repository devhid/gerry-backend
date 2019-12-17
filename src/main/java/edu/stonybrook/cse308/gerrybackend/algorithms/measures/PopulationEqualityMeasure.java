package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationEquality;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.MathUtils;

import java.util.Set;

public interface PopulationEqualityMeasure {

    class MostToLeast {
        public static double computePopulationEqualityScore(StateNode state) {
            DistrictNode mostPopulated = getMostPopulatedDistrict(state.getChildren());
            DistrictNode leastPopulated = getLeastPopulatedDistrict(state.getChildren());
            return computePercentDifference(mostPopulated, leastPopulated);
        }

        public static double computeProposedPopulationEqualityScore(StateNode state) {
            DistrictNode mostPopulated = getMostPopulatedDistrict(state.getProposedDistricts());
            DistrictNode leastPopulated = getLeastPopulatedDistrict(state.getProposedDistricts());
            return computePercentDifference(mostPopulated, leastPopulated);
        }

        private static DistrictNode getExtremePopulationDistrict(Set<DistrictNode> districts, boolean mostPopulated) {
            DistrictNode extremeDistrict = null;
            int currentExtremePop = (mostPopulated) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            for (DistrictNode d : districts) {
                int districtPop = d.getDemographicData().getTotalPopulation();
                if (mostPopulated) {
                    extremeDistrict = (districtPop > currentExtremePop) ? d : extremeDistrict;
                    currentExtremePop = districtPop;
                } else {
                    extremeDistrict = (districtPop < currentExtremePop) ? d : extremeDistrict;
                    currentExtremePop = districtPop;
                }
            }
            return extremeDistrict;
        }

        private static DistrictNode getMostPopulatedDistrict(Set<DistrictNode> districts) {
            return MostToLeast.getExtremePopulationDistrict(districts, true);
        }

        private static DistrictNode getLeastPopulatedDistrict(Set<DistrictNode> districts) {
            return MostToLeast.getExtremePopulationDistrict(districts, false);
        }

        private static double computePercentDifference(DistrictNode mostPopulated, DistrictNode leastPopulated) {
            int maxPopulation = mostPopulated.getDemographicData().getTotalPopulation();
            int leastPopulation = leastPopulated.getDemographicData().getTotalPopulation();
            return MathUtils.calculatePercentDifference(maxPopulation, leastPopulation);
        }
    }

    class Ideal {
        public static double computePopulationEqualityScore(DistrictNode district) {
            StateNode state = district.getParent();
            double idealPopulation = (double) state.getDemographicData().getTotalPopulation() / state.getChildren().size();
            int truePopulation = district.getDemographicData().getTotalPopulation();
//            double percentDiff = MathUtils.calculatePercentDifference(idealPopulation, truePopulation);
            double squaredDiff = Math.pow(Math.abs(idealPopulation - truePopulation) / idealPopulation, 2);
            return 1.0 - squaredDiff;
        }
    }

    static double computePopulationEqualityScore(PopulationEquality measure, DistrictNode district) {
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

    static double computePopulationEqualityScore(PopulationEquality measure, StateNode state) {
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

    static double computeProposedPopulationEqualityScore(PopulationEquality measure, StateNode state) {
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
