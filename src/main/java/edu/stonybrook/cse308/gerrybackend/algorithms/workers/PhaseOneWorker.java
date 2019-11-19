package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.Heuristics;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.*;

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
        return state.getChildren().size() - pairs.size() <= numDistricts;
    }

    private void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges){
        Heuristics.filterLastIterationPairs(heuristic, pairs, numAllowedMerges);
    }

    private PhaseOneMergeDelta joinCandidatePairs(StateNode state, CandidatePairs pairs, int iteration){
        Map<DistrictNode, DistrictNode> mergedDistricts = new HashMap<>();
        Map<UnorderedPair<String>, DistrictNode> mergedDistrictsById = new HashMap<>();
        for (LikelyCandidatePair pair : pairs.getAllPairs()){
            try {
                DistrictNode d1 = pair.getItem1();
                DistrictNode d2 = pair.getItem2();
                DistrictNode newDistrict = DistrictNode.combine(d1, d2);
                mergedDistricts.put(d1, newDistrict);
                mergedDistricts.put(d2, newDistrict);
                mergedDistrictsById.put(new UnorderedPair<>(d1.getId(), d2.getId()), newDistrict);
            } catch (MismatchedElectionException e) {
                // should never happen
                e.printStackTrace();
            }
        }
        state.remapDistrictReferences(mergedDistricts);
        return new PhaseOneMergeDelta(iteration, mergedDistrictsById, new HashSet<>(mergedDistricts.values()));
    }

    @Override
    public PhaseOneReport run(PhaseOneInputs inputs) {
        List<PhaseOneMergeDelta> deltas = new ArrayList<>();
        StateNode state = inputs.getState();
        int numDistricts = inputs.getNumDistricts();
        int iteration = 0;
        while (state.getChildren().size() != numDistricts){
            CandidatePairs pairs = determineCandidatePairs(inputs.getPhaseOneMajMinPairsHeuristic(),
                    inputs.getPhaseOneOtherPairsHeuristic(), inputs.getState());
            boolean lastIteration = isLastIteration(state, pairs, numDistricts);
            if (lastIteration){
                int numAllowedMerges = state.getChildren().size() - numDistricts;
                filterLastIterationPairs(inputs.getStopHeuristic(), pairs, numAllowedMerges);
            }
            PhaseOneMergeDelta iterationDelta = joinCandidatePairs(state, pairs, iteration);
            deltas.add(iterationDelta);
            iteration++;
        }
        return new PhaseOneReport(deltas);
    }
}
