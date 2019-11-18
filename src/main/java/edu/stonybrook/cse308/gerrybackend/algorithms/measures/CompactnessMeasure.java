package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.CompactnessEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

public interface CompactnessMeasure {

    class GraphTheoretical {
        static double computeCompactnessScore(DistrictNode district){
            // TODO: fill in
            return -1.0;
        }
    }

    class PolsbyPopper {
        static double computeCompactnessScore(DistrictNode district){
            // TODO: fill in
            return -1.0;
        }
    }

    class Schwartzberg {
        private static double computePerimeter(DistrictNode district){
            return -1.0;
        }

        private static double computeCircumferenceOfSameAreaCircle(DistrictNode district){
            return -1.0;
        }

        static double computeCompactnessScore(DistrictNode district){
            // TODO: fill in
            return -1.0;
        }
    }

    static double computeCompactnessScore(CompactnessEnum measure, DistrictNode district){
        double compactnessScore = 0.0;
        switch (measure){
            case GRAPH_THEORETICAL:
                compactnessScore = GraphTheoretical.computeCompactnessScore(district);
                break;
            case POLSBY_POPPER:
                compactnessScore = PolsbyPopper.computeCompactnessScore(district);
                break;
            case SCHWARTZBERG:
                compactnessScore = Schwartzberg.computeCompactnessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return compactnessScore;
    }
}
