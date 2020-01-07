package edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.comparators.SmallestLikelyPairsComparator;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import net.bytebuddy.pool.TypePool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface PhaseOneStopHeuristic {

    class JoinSmallest {
        private static void filterSmallestAllowedMerges(CandidatePairs pairs, int numAllowedMerges) {
            if (numAllowedMerges < 0) {
                throw new IllegalArgumentException("Replace this string later!");
            } else if (numAllowedMerges > pairs.size()) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            List<LikelyCandidatePair> listPairs = new ArrayList<>(pairs.getMajorityMinorityPairs());
            SmallestLikelyPairsComparator comparator = new SmallestLikelyPairsComparator();
            if (numAllowedMerges > listPairs.size()) {
                List<LikelyCandidatePair> otherPairs = new ArrayList<>(pairs.getOtherPairs());
                otherPairs.sort(comparator);
                List<LikelyCandidatePair> firstFewPairs = otherPairs.subList(0, numAllowedMerges - listPairs.size());
                listPairs.addAll(firstFewPairs);
            }
            listPairs.sort(comparator);
            List<LikelyCandidatePair> filteredPairs = listPairs.subList(0, numAllowedMerges);
            pairs.filterPairs(filteredPairs);
        }
    }

    static void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges) {
        switch (heuristic) {
            case JOIN_SMALLEST:
                JoinSmallest.filterSmallestAllowedMerges(pairs, numAllowedMerges);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
    }

}
