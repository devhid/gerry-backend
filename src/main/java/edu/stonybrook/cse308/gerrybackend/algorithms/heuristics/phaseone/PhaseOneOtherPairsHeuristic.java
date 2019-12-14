package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PhaseOneOtherPairsHeuristic {

    class Standard {
        private static LikelyType determineLikelyType(double joinability) {
            if (joinability >= 0.0 && joinability < 1.0) {
                return LikelyType.NOT;
            }

            if (joinability >= 1.0 && joinability < 2.0) {
                return LikelyType.KIND_OF;
            }

            if (joinability >= 2.0 && joinability <= 3.0) {
                return LikelyType.VERY;
            }

            // should never happen, because joinability without minority should be <= 0 and <= 3,
            // but added as a sanity check.
            throw new IllegalArgumentException("Replace string with text later! " + joinability);
        }

        private static LikelyCandidatePair createLikelyCandidatePair(DistrictNode d1, DistrictNode d2) {
            // Find the edge that is shared by both districts.
            DistrictEdge shared = (DistrictEdge) d1.getEdge(d2);

            // Compute its joinability.
            Set<PoliticalParty> parties = d2.getElectionData().getWinners();
            double joinability = shared.getJoinabilityValue(parties);

            // Determine the likely type based off joinability.
            LikelyType likelyType = determineLikelyType(joinability);

            return new LikelyCandidatePair(d1, d2, likelyType);
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
            DistrictNode d4 = (oldD2Pair.getItem1() == d2) ? oldD1Pair.getItem2() : oldD1Pair.getItem1();
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
            Set<DistrictNode> allDistricts = new HashSet<>(inputs.getState().getChildren());
            allDistricts.removeAll(usedDistricts);

            Queue<DistrictNode> consideredDistricts = new LinkedList<>(allDistricts);

            Map<DistrictNode, LikelyCandidatePair> likelyPairs = new HashMap<>();

            while (!consideredDistricts.isEmpty()) {
                DistrictNode d1 = consideredDistricts.poll();

                Set<DistrictNode> adjNodes = GenericUtils.castSetOfObjects(d1.getAdjacentNodes(), DistrictNode.class);

                for (DistrictNode d2 : adjNodes) {
                    LikelyCandidatePair likelyPair = Standard.createLikelyCandidatePair(d1, d2);
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
                                takePair = Standard.checkBothDistrictsPaired(likelyPair, likelyPairs, consideredDistricts);
                            }
                        } else if (d1Paired) {
                            takePair = Standard.checkOnlyOneDistrictPaired(d1, likelyType, likelyPairs, consideredDistricts);
                        } else {
                            takePair = Standard.checkOnlyOneDistrictPaired(d2, likelyType, likelyPairs, consideredDistricts);
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