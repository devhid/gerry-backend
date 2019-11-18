package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.Heuristics;
import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phaseone.PhaseOneStopHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PhaseOneWorker extends AlgPhaseWorker<PhaseOneInputs, PhaseOneReport> {

    private Set<LikelyCandidatePair> determineMajorityMinorityPairs(PhaseOneMajMinPairs majMinPairsHeuristic, StateNode state){
        return Heuristics.determineMajMinPairs(majMinPairsHeuristic, state);
    }

    private Set<LikelyCandidatePair> determineOtherPairs(PhaseOneOtherPairs otherPairsHeuristic,
                                                                 StateNode state,
                                                                 Set<LikelyCandidatePair> majMinPairs){
        return Heuristics.determineOtherPairs(otherPairsHeuristic, state, majMinPairs);
    }

    private CandidatePairs determineCandidatePairs(PhaseOneMajMinPairs majMinPairsHeuristic,
                                                   PhaseOneOtherPairs otherPairsHeuristic, StateNode state){
        Set<LikelyCandidatePair> majMinPairs = this.determineMajorityMinorityPairs(majMinPairsHeuristic, state);
        Set<LikelyCandidatePair> otherPairs = this.determineOtherPairs(otherPairsHeuristic, state, majMinPairs);
        return new CandidatePairs(majMinPairs, otherPairs);
    }

    private boolean isLastIteration(StateNode state, CandidatePairs pairs, int numDistricts){
        return state.getNodes().size() - pairs.size() <= numDistricts;
    }

    private void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges){
        Heuristics.filterLastIterationPairs(heuristic, pairs, numAllowedMerges);
    }

    private PhaseOneMergeDelta joinCandidatePairs(StateNode state, CandidatePairs pairs, int iteration){
        // TODO: fill in
        return null;
    }

    @Override
    public PhaseOneReport run(PhaseOneInputs inputs) {
        List<PhaseOneMergeDelta> deltas = new ArrayList<>();
        StateNode state = inputs.getState();
        int numDistricts = inputs.getNumDistricts();
        int iteration = 0;
        while (state.getNodes().size() != numDistricts){
            CandidatePairs pairs = determineCandidatePairs(inputs.getPhaseOneMajMinPairsHeuristic(),
                    inputs.getPhaseOneOtherPairsHeuristic(), inputs.getState());
            boolean lastIteration = isLastIteration(state, pairs, numDistricts);
            if (lastIteration){
                int numAllowedMerges = state.getNodes().size() - numDistricts;
                filterLastIterationPairs(inputs.getStopHeuristic(), pairs, numAllowedMerges);
            }
            PhaseOneMergeDelta iterationDelta = joinCandidatePairs(state, pairs, iteration);
            deltas.add(iterationDelta);
            iteration++;
        }
        return new PhaseOneReport(deltas);
    }
}
