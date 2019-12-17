package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationHomogeneity;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;
import edu.stonybrook.cse308.gerrybackend.utils.GraphUtils;
import edu.stonybrook.cse308.gerrybackend.utils.MathUtils;
import edu.stonybrook.cse308.gerrybackend.utils.RandomUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface PhaseTwoPrecinctMoveHeuristic {

    class Random {
        private static PrecinctMove selectPrecinct(StateNode state) {
            DistrictNode from = RandomUtils.getRandomElement(state.getChildren());
            PrecinctNode selected = RandomUtils.getRandomElement(from.getBorderPrecincts());
            Set<DistrictNode> adjDistricts = selected.getAdjacentNodes().stream()
                    .map(p -> (DistrictNode) p.getParent()).collect(Collectors.toSet());
            adjDistricts.remove(from);
            DistrictNode to = RandomUtils.getRandomElement(adjDistricts);
            return new PrecinctMove(from, to, selected);
        }
    }

    class MajorityMinority {
        private static PrecinctMove selectPrecinct(StateNode state, Set<DemographicType> demographicTypes,
                                                   double lowerBound, double upperBound) {
            DistrictNode from = null;
            DistrictNode to = null;
            PrecinctNode selected = null;
            DemographicData fromData = null;
            double fromRatio = 0;
            double selectedFromRatio = 0;
            for (DistrictNode d : state.getChildren()) {
                fromRatio = ((double) d.getDemographicData().getDemoPopulation(demographicTypes))
                        / d.getDemographicData().getTotalPopulation();
                selectedFromRatio = fromRatio;
                if (fromRatio < upperBound) {
                    from = d;
                    fromData = from.getDemographicData();
                    break;
                }
            }
            if (from == null) {
                return null;
            }
            for (PrecinctNode precinct: from.getBorderPrecincts()) {
                DemographicData potentialFromData = new DemographicData(fromData);
                potentialFromData.subtract(precinct.getDemographicData());
                double potentialFromRatio = ((double) potentialFromData.getDemoPopulation(demographicTypes))
                        / potentialFromData.getTotalPopulation();
                if (potentialFromRatio > selectedFromRatio) {
                    selected = precinct;
                    selectedFromRatio = potentialFromRatio;
                }
            }

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

    class PopulationNormalizer {
        private static PrecinctMove selectPrecinct(StateNode state) {
            DistrictNode from = null;
            DistrictNode to = null;
            PrecinctNode selected = null;
            int fromPop = 0;

//            Set<DistrictNode> bigDistricts = new HashSet<>(state.getChildren());
//            Set<DistrictNode> bigDistricts = new HashSet<>();
//            double idealPop = ((double) state.getDemographicData().getTotalPopulation()) / state.getChildren().size();
            for (DistrictNode d : state.getChildren()) {
                int dPop = d.getDemographicData().getTotalPopulation();
                if (dPop > fromPop) {
                    from = d;
                    fromPop = dPop;
                }
            }
            if (from == null) {
                return null;
            }
//            if (bigDistricts.size() == 0) {
//                return null;
//            }
//            from = RandomUtils.getRandomElement(bigDistricts);
//            fromPop = from.getDemographicData().getTotalPopulation();

            Set<DistrictNode> smallerDistricts = GenericUtils.castSetOfObjects(from.getAdjacentNodes(), DistrictNode.class);

//            Set<DistrictNode> smallerDistricts = new HashSet<>();
//
//            for (DistrictNode adjDistrict : GenericUtils.castSetOfObjects(from.getAdjacentNodes(), DistrictNode.class)) {
//                int adjDistrictPop = adjDistrict.getDemographicData().getTotalPopulation();
//                if (adjDistrictPop < fromPop) {
//                    smallerDistricts.add(adjDistrict);
//                }
//            }
//            if (smallerDistricts.size() == 0) {
//                return null;
//            }

            to = RandomUtils.getRandomElement(smallerDistricts);
            Set<PrecinctNode> fromBorderPrecincts = from.getBorderPrecincts();
            Set<PrecinctNode> toBorderPrecincts = to.getBorderPrecincts();
            Set<PrecinctNode> candidateSelections = new HashSet<>();
            for (PrecinctNode fromBorderPrecinct : fromBorderPrecincts) {
                Set<PrecinctNode> adjFromBorderPrecincts = GenericUtils.castSetOfObjects(fromBorderPrecinct.getAdjacentNodes(), PrecinctNode.class);
                adjFromBorderPrecincts.retainAll(toBorderPrecincts);
                if (adjFromBorderPrecincts.size() > 0) {
                    if (!GraphUtils.isBridgePrecinct(from, fromBorderPrecinct)) {
                        candidateSelections.add(fromBorderPrecinct);
                    }
//                    try {
//                        Geometry multiPolygon = from.getMultiPolygon();
//                        multiPolygon = multiPolygon.difference(fromBorderPrecinct.getGeometry());
//                        if (multiPolygon.union().isValid()) {
//                            candidateSelections.add(fromBorderPrecinct);
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                }
            }
            if (candidateSelections.size() == 0) {
                return null;
            }
            selected = RandomUtils.getRandomElement(candidateSelections);
//            double minPercentPopDiff = Double.MAX_VALUE;
//            for (PrecinctNode candidate : candidateSelections) {
//                int candidatePop = candidate.getDemographicData().getTotalPopulation();
//                double potentialPopDiff = MathUtils.calculatePercentDifference(idealPop, fromPop - candidatePop);
//                if (potentialPopDiff < minPercentPopDiff) {
//                    selected = candidate;
//                    minPercentPopDiff = potentialPopDiff;
//                }
//            }
            return new PrecinctMove(from, to, selected);
        }
    }

    static PrecinctMove selectPrecinct(PhaseTwoInputs inputs) {
        PrecinctMove precinctMove;
        switch (inputs.getMoveHeuristic()) {
            case RANDOM:
                precinctMove = Random.selectPrecinct(inputs.getState());
                break;
            case MAJ_MIN:
                precinctMove = MajorityMinority.selectPrecinct(inputs.getState(), inputs.getDemographicTypes(),
                        inputs.getLowerBound(), inputs.getUpperBound());
                break;
            case POP_NORMALIZER:
                precinctMove = PopulationNormalizer.selectPrecinct(inputs.getState());
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return precinctMove;
    }

}