package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalFairnessEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PoliticalFairnessMeasure {

    class EfficiencyGap {
        public static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            return -1.0;
        }
    }

    class GerrymanderDemocrat {
        public static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            return -1.0;
        }
    }

    class GerrymanderRepublican {
        public static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            return -1.0;
        }
    }

    class LopsidedMargins {
        public static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            return -1.0;
        }
    }

    class MeanMedian {
        public static double computeFairnessScore(StateNode state) {
            // TODO: fill in
            return -1.0;
        }
    }

    class Partisan {
        public static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            return -1.0;
        }
    }

    static double computeFairnessScore(PoliticalFairnessEnum measure, DistrictNode district) {
        double fairnessScore = 0.0;
        switch (measure) {
            case EFFICIENCY_GAP:
                fairnessScore = EfficiencyGap.computeFairnessScore(district);
                break;
            case GERRYMANDER_DEMOCRAT:
                fairnessScore = GerrymanderDemocrat.computeFairnessScore(district);
                break;
            case GERRYMANDER_REPUBLICAN:
                fairnessScore = GerrymanderRepublican.computeFairnessScore(district);
                break;
            case LOPSIDED_MARGINS:
                fairnessScore = LopsidedMargins.computeFairnessScore(district);
                break;
            case PARTISAN:
                fairnessScore = Partisan.computeFairnessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return fairnessScore;
    }

    static double computeFairnessScore(PoliticalFairnessEnum measure, StateNode state) {
        double fairnessScore = 0.0;
        switch (measure) {
            case MEAN_MEDIAN:
                fairnessScore = MeanMedian.computeFairnessScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return fairnessScore;
    }

}
