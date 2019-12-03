package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;

import java.util.*;

public interface PhaseOneMajorityMinorityPairsHeuristic {

    class Standard {

        private static LikelyCandidatePair createLikelyCandidatePair(PhaseOneInputs inputs, DistrictNode d1,
                                                                     DistrictNode d2) {
            DemographicData d1Demo = d1.getDemographicData();
            DemographicData d2Demo = d2.getDemographicData();
            double d1DemoPop = d1Demo.getDemoPopulation(inputs.getDemographicTypes());
            double d2DemoPop = d2Demo.getDemoPopulation(inputs.getDemographicTypes());
            double d1TotalPop = d1Demo.getTotalPopulation();
            double d2TotalPop = d2Demo.getTotalPopulation();
            double potentialRatio = (d1DemoPop + d2DemoPop) / (d1TotalPop + d2TotalPop);
            double currentRatio = d1DemoPop / d1TotalPop;
            LikelyType likelyType;
            if (currentRatio < inputs.getLowerBound()) {
                likelyType = (potentialRatio >= currentRatio) ? LikelyType.VERY : LikelyType.NOT;
            } else if (currentRatio > inputs.getUpperBound()) {
                likelyType = (potentialRatio <= currentRatio) ? LikelyType.VERY : LikelyType.NOT;
            } else {
                likelyType = ((inputs.getLowerBound() <= potentialRatio) &&
                        (potentialRatio <= inputs.getUpperBound())) ? LikelyType.KIND_OF : LikelyType.NOT;
            }
            return new LikelyCandidatePair(d1, d2, likelyType);
        }

        private static boolean checkBothDistrictsPaired(LikelyCandidatePair likelyPair,
                                                        Map<DistrictNode, LikelyCandidatePair> likelyPairs,
                                                        Set<LikelyCandidatePair> kindOfLikelyPairs,
                                                        Queue<DistrictNode> districtsToConsider) {
            DistrictNode d1 = likelyPair.getItem1();
            DistrictNode d2 = likelyPair.getItem2();
            LikelyType likelyType = likelyPair.getLikelyType();
            LikelyCandidatePair oldD1Pair = likelyPairs.get(d1);
            LikelyCandidatePair oldD2Pair = likelyPairs.get(d2);
            if (oldD1Pair.getLikelyType().isGreaterThanOrEqualTo(likelyType)) {
                return false;
            }
            if (oldD2Pair.getLikelyType().isGreaterThanOrEqualTo(likelyType)) {
                return false;
            }
            kindOfLikelyPairs.remove(oldD1Pair);
            kindOfLikelyPairs.remove(oldD2Pair);
            DistrictNode d3 = (oldD1Pair.getItem1() == d1) ? oldD1Pair.getItem2() : oldD1Pair.getItem1();
            DistrictNode d4 = (oldD2Pair.getItem1() == d2) ? oldD1Pair.getItem2() : oldD1Pair.getItem1();
            likelyPairs.remove(d3);
            likelyPairs.remove(d4);
            districtsToConsider.add(d3);
            districtsToConsider.add(d4);
            return true;
        }

        private static boolean checkOnlyOneDistrictPaired(DistrictNode d, LikelyType likelyType,
                                                          Map<DistrictNode, LikelyCandidatePair> likelyPairs,
                                                          Set<LikelyCandidatePair> kindOfLikelyPairs,
                                                          Queue<DistrictNode> districtsToConsider) {
            if (likelyPairs.get(d).getLikelyType().isGreaterThanOrEqualTo(likelyType)) {
                return false;
            }
            LikelyCandidatePair oldPair = likelyPairs.get(d);
            DistrictNode otherDistrict = (oldPair.getItem1() == d) ? oldPair.getItem2() : oldPair.getItem1();
            kindOfLikelyPairs.remove(oldPair);
            likelyPairs.remove(otherDistrict);
            districtsToConsider.offer(otherDistrict);
            return true;
        }

        public static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs) {
            StateNode state = inputs.getState();
            Map<DistrictNode, LikelyCandidatePair> likelyPairs = new HashMap<>();
            Set<LikelyCandidatePair> kindOfLikelyPairs = new HashSet<>();
            Set<LikelyCandidatePair> veryLikelyPairs = new HashSet<>();
            Queue<DistrictNode> districtsToConsider = new LinkedList<>(state.getChildren());
            while (!districtsToConsider.isEmpty()) {
                DistrictNode d1 = districtsToConsider.poll();
                Set<DistrictNode> adjNodes = GenericUtils.castSetOfObjects(d1.getAdjacentNodes(), DistrictNode.class);
                for (DistrictNode d2 : adjNodes) {
                    LikelyCandidatePair likelyPair = Standard.createLikelyCandidatePair(inputs, d1, d2);
                    LikelyType likelyType = likelyPair.getLikelyType();
                    if (likelyType == LikelyType.NOT) {
                        continue;
                    }
                    boolean takePair = true;
                    boolean d1Paired = likelyPairs.containsKey(d1);
                    boolean d2Paired = likelyPairs.containsKey(d2);
                    if (d1Paired || d2Paired) {
                        if (likelyType == LikelyType.KIND_OF && kindOfLikelyPairs.contains(likelyPair)) {
                            continue;
                        }
                        if (likelyType == LikelyType.VERY && veryLikelyPairs.contains(likelyPair)) {
                            continue;
                        } else if (d1Paired && d2Paired) {
                            takePair = Standard.checkBothDistrictsPaired(likelyPair, likelyPairs, kindOfLikelyPairs,
                                    districtsToConsider);
                        } else if (d1Paired) {
                            takePair = Standard.checkOnlyOneDistrictPaired(d1, likelyType, likelyPairs,
                                    kindOfLikelyPairs, districtsToConsider);
                        } else {
                            takePair = Standard.checkOnlyOneDistrictPaired(d2, likelyType, likelyPairs,
                                    kindOfLikelyPairs, districtsToConsider);
                        }
                    }
                    if (takePair) {
                        switch (likelyType) {
                            case KIND_OF:
                                kindOfLikelyPairs.add(likelyPair);
                                break;
                            case VERY:
                                veryLikelyPairs.add(likelyPair);
                                break;
                        }
                        likelyPairs.put(d1, likelyPair);
                        likelyPairs.put(d2, likelyPair);
                    }
                }
            }
            return null;
        }
    }

    static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs) {
        Set<LikelyCandidatePair> pairs;
        switch (inputs.getMajMinPairsHeuristic()) {
            case STANDARD:
                pairs = Standard.determinePairs(inputs);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return pairs;
    }
}
