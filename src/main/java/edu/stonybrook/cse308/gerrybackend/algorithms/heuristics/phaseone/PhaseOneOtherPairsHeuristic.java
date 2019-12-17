package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.graph.Joinability;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PhaseOneOtherPairsHeuristic {

    class Standard {
        public static LikelyType getLikelyTypeFromJoinabilityWithoutMinority(double joinability) {
            if (joinability < 0.5) {
                return LikelyType.NOT;
            }

            if (joinability >= 0.5 && joinability < 1.5) {
                return LikelyType.KIND_OF;
            }

            return LikelyType.VERY;
        }

        private static LikelyCandidatePair createLikelyCandidatePair(DistrictNode d1, DistrictNode d2, Set<DemographicType> demoTypes) {
            // Find the edge that is shared by both districts.
            DistrictEdge shared = (DistrictEdge) d1.getEdge(d2);

            // Get the joinability factors.
            Joinability joinability = shared.getJoinability();
            double populationJoinability = joinability.getPopulation(); // [0,2]
            double compactnessJoinability = joinability.getCompactness();   // [0,1]
//            double minorityJoinability = joinability.getMinority(demoTypes);
//            double politicalJoinability = joinability.getPolitical(d1.getElectionData().getWinners());  // [0,1]
//            politicalJoinability = (politicalJoinability + joinability.getPolitical(d2.getElectionData().getWinners()));
//            politicalJoinability = politicalJoinability / 2;

//            // Override the population joinability likeliness if the merge would get too big.
//            double d1TotalPop = d1.getDemographicData().getTotalPopulation();
//            double d2TotalPop = d2.getDemographicData().getTotalPopulation();
//            double totalPop = d1TotalPop + d2TotalPop;
//            double idealPop = d1.getParent().getDemographicData().getTotalPopulation();
//            if (totalPop > 1.07 * idealPop) {
//                double popPercentDiff = MathUtils.calculatePercentDifference(d1TotalPop, d2TotalPop);
//                if (popPercentDiff > 1.0) {
//                    populationJoinability *= 1.5;
//                } else if (popPercentDiff > 0.5 && popPercentDiff <= 1.0){
//                    populationJoinability *= 0.5;
//                } else {
//                    populationJoinability = 0.0;
//                }
//            }

            // Determine how likely we are to make this pairing.
            double joinabilityScore = populationJoinability + compactnessJoinability;
            LikelyType likelyType = Standard.getLikelyTypeFromJoinabilityWithoutMinority(joinabilityScore);

            return new LikelyCandidatePair(d1, d2, likelyType, joinabilityScore);
        }

        // Maybe more into a PhaseOneUtils class.
        private static boolean checkBothDistrictsPaired(LikelyCandidatePair likelyPair,
                                                        Map<DistrictNode, LikelyCandidatePair> likelyPairs,
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
            DistrictNode d3 = (oldD1Pair.getItem1() == d1) ? oldD1Pair.getItem2() : oldD1Pair.getItem1();
            DistrictNode d4 = (oldD2Pair.getItem1() == d2) ? oldD2Pair.getItem2() : oldD2Pair.getItem1();
            likelyPairs.remove(d3);
            likelyPairs.remove(d4);
            districtsToConsider.add(d3);
            districtsToConsider.add(d4);
            return true;
        }

        // Maybe more into a PhaseOneUtils class.
        private static boolean checkOnlyOneDistrictPaired(DistrictNode d, LikelyType likelyType,
                                                          Map<DistrictNode, LikelyCandidatePair> likelyPairs,
                                                          Queue<DistrictNode> districtsToConsider) {
            if (likelyPairs.get(d).getLikelyType().isGreaterThanOrEqualTo(likelyType)) {
                return false;
            }
            LikelyCandidatePair oldPair = likelyPairs.get(d);
            DistrictNode otherDistrict = (oldPair.getItem1() == d) ? oldPair.getItem2() : oldPair.getItem1();
            likelyPairs.remove(otherDistrict);
            districtsToConsider.offer(otherDistrict);
            return true;
        }

        public static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs,
                                                              Set<LikelyCandidatePair> majMinPairs) {
            Set<DistrictNode> usedDistricts = majMinPairs
                    .stream()
                    .flatMap(district -> Stream.of(district.getItem1(), district.getItem2()))
                    .collect(Collectors.toSet());

            // consider districts that are non-majority-minority
            Set<DistrictNode> remainingDistricts = new HashSet<>(inputs.getState().getChildren());
            remainingDistricts.removeAll(usedDistricts);
            Queue<DistrictNode> districtsToConsider = new LinkedList<>(remainingDistricts);

            Map<DistrictNode, LikelyCandidatePair> likelyPairs = new HashMap<>();
            while (!districtsToConsider.isEmpty()) {
                DistrictNode d1 = districtsToConsider.poll();
                Set<DistrictNode> adjNodes = GenericUtils.castSetOfObjects(d1.getAdjacentNodes(), DistrictNode.class);
                adjNodes.retainAll(remainingDistricts);
                for (DistrictNode d2 : adjNodes) {
                    LikelyCandidatePair likelyPair = Standard.createLikelyCandidatePair(d1, d2, inputs.getDemographicTypes());
                    LikelyType likelyType = likelyPair.getLikelyType();
                    if (likelyType == LikelyType.NOT) {
                        continue;
                    }

                    boolean takePair = true;
                    boolean d1Paired = likelyPairs.containsKey(d1);
                    boolean d2Paired = likelyPairs.containsKey(d2);
                    if (d1Paired || d2Paired) {
                        if (d1Paired && d2Paired) {
                            if (likelyPairs.get(d1) == likelyPair) {
                                continue;
                            } else {
                                takePair = Standard.checkBothDistrictsPaired(likelyPair, likelyPairs, districtsToConsider);
                            }
                        } else if (d1Paired) {
                            takePair = Standard.checkOnlyOneDistrictPaired(d1, likelyType, likelyPairs, districtsToConsider);
                        } else {
                            takePair = Standard.checkOnlyOneDistrictPaired(d2, likelyType, likelyPairs, districtsToConsider);
                        }
                    }
                    if (takePair) {
                        likelyPairs.put(d1, likelyPair);    // create or update mapping for d1
                        likelyPairs.put(d2, likelyPair);    // create or update mapping for d2
                    }
                }
            }
            return new HashSet<>(likelyPairs.values());
        }
    }

    static Set<LikelyCandidatePair> determinePairs(PhaseOneInputs inputs,
                                                   Set<LikelyCandidatePair> majMinPairs) {
        Set<LikelyCandidatePair> pairs;
        switch (inputs.getOtherPairsHeuristic()) {
            case STANDARD:
                pairs = Standard.determinePairs(inputs, majMinPairs);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return pairs;
    }
}