package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.CompactnessEnum;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface CompactnessMeasure {

    class GraphTheoretical {
        static double computeCompactnessScore(StateNode state){
            // TODO: fill in
            return -1.0;
        }
    }

    class PolsbyPopper {
        static double computeCompactnessScore(StateNode state){
            // TODO: fill in
            return -1.0;
        }
    }

    class Schwartzberg {
        static double computeCompactnessScore(StateNode state){
            // TODO: fill in
            return -1.0;
        }
    }

    static double computeCompactnessScore(CompactnessEnum measure, StateNode state){
        double compactnessScore = 0.0;
        switch (measure){
            case GRAPH_THEORETICAL:
                compactnessScore = GraphTheoretical.computeCompactnessScore(state);
                break;
            case POLSBY_POPPER:
                compactnessScore = PolsbyPopper.computeCompactnessScore(state);
                break;
            case SCHWARTZBERG:
                compactnessScore = Schwartzberg.computeCompactnessScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return compactnessScore;
    }
}
