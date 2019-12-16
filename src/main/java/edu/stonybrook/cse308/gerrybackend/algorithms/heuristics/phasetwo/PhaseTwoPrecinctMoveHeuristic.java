package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;

import java.util.Collection;
import java.util.Set;

public interface PhaseTwoPrecinctMoveHeuristic {

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

    class Random {
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

    class MajorityMinority {
        private static PrecinctMove selectPrecinct(StateNode state, Set<DemographicType> demographicTypes) {
            DistrictNode from = getRandomDistrict(state.getChildren());
            DistrictNode to = getRandomDistrict(from.getAdjacentNodes());

            double maxJoinability = -1.0;
            PrecinctNode selected = null;

//            for(PrecinctNode precinct: from.getBorderPrecincts()) {
//                for(PrecinctEdge edge: precinct.getAdjacentEdges()) {
//                    double joinability = edge.getJoinabilityValue(precinct.getElectionData().getWinners(), demographicTypes);
//
//                    // Select the node if it has a higher joinability.
//                    if(joinability > maxJoinability) {
//                        selected = precinct;
//                    }
//                }
//            }

//            for(PrecinctNode precinct: from.getBorderPrecincts()) {
//                // Loop through all adjacent nodes of each border precinct.
//                for(PrecinctNode node: GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class)) {
//                    // Ignore the precincts that are already within the same district.
//                    if(from.getChildren().contains(node)) {
//                        continue;
//                    }
//
//                    // Get the edge that connects the border precinct and the adjacent node.
//                    GerryEdge edge = precinct.getEdge(node);
//
//                    // Calculate its joinability using the new node's party winners.
//                    double joinability = edge.getJoinabilityValue(node.getElectionData().getWinners(), demographicTypes);
//
//                    // Select the node if it has a higher joinability.
//                    if(joinability > maxJoinability) {
//                        selected = precinct;
//                    }
//                }
//            }

            return new PrecinctMove(from, to, selected);
        }
    }

    static PrecinctMove selectPrecinct(PhaseTwoPrecinctMove heuristic, StateNode state, PhaseTwoInputs inputs) {
        PrecinctMove precinctMove;
        switch (heuristic) {
            case RANDOM:
                precinctMove = Random.selectPrecinct(state);
                break;
            case MAJ_MIN:
                precinctMove = MajorityMinority.selectPrecinct(state, inputs.getDemographicTypes());
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return precinctMove;
    }

}