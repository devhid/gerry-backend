package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.Collection;

public interface PhaseTwoPrecinctMoveHeuristic {

    class Random {
        private static int randomIndex(Collection collection) {
            return (int) Math.floor(Math.random() * collection.size());
        }

        private static DistrictNode getRandomDistrict(Collection collection) throws IllegalArgumentException {
            int districtIndex = randomIndex(collection);
            int currentIndex = 0;

            for(Object district: collection) {
                if(currentIndex == districtIndex) {
                    return (DistrictNode) district;
                }

                currentIndex += 1;
            }

            // should never be thrown
            throw new IllegalArgumentException("Replace this string later!");
        }

        private static PrecinctMove selectPrecinct(StateNode state) {
            DistrictNode from = getRandomDistrict(state.getChildren());
            DistrictNode to = getRandomDistrict(from.getAdjacentNodes());

            int precinctIndex = randomIndex(from.getBorderPrecincts());
            int currentIndex = 0;

            PrecinctNode selected = null;
            for(PrecinctNode precinct: from.getBorderPrecincts()) {
                if (currentIndex == precinctIndex) {
                    selected = precinct;
                    break;
                }

                currentIndex += 1;
            }

            return new PrecinctMove(from, to, selected);
        }
    }

    static PrecinctMove selectPrecinct(PhaseTwoPrecinctMove heuristic, StateNode state) {
        PrecinctMove precinctMove;
        switch (heuristic) {
            case RANDOM:
                precinctMove = Random.selectPrecinct(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return precinctMove;
    }

}