package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PhaseOneWorker extends AlgPhaseWorker<PhaseOneInputs, PhaseOneReport> {

    private Set<UnorderedPair<DistrictNode>> determineMajorityMinorityPairs(){
        // TODO: fill in
        return null;
    }

    private Set<UnorderedPair<DistrictNode>> determineOtherPairs(){
        // TODO: fill in
        return null;
    }

    private CandidatePairs determineCandidatePairs(){
        Set<UnorderedPair<DistrictNode>> majMinPairs = this.determineMajorityMinorityPairs();
        Set<UnorderedPair<DistrictNode>> otherPairs = this.determineOtherPairs();
        return new CandidatePairs(majMinPairs, otherPairs);
    }

    private boolean isLastIteration(StateNode state, CandidatePairs pairs, int numDistricts){
        return state.getNodes().size() - pairs.size() <= numDistricts;
    }

    private void filterLastIterationPairs(CandidatePairs pairs, PhaseOneStop heuristic){
        // TODO: fill in
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
            CandidatePairs pairs = determineCandidatePairs();
            boolean lastIteration = isLastIteration(state, pairs, numDistricts);
            if (lastIteration){
                filterLastIterationPairs(pairs, inputs.getStopHeuristic());
            }
            PhaseOneMergeDelta iterationDelta = joinCandidatePairs(state, pairs, iteration);
            deltas.add(iterationDelta);
            iteration++;
        }
        return new PhaseOneReport(deltas);
    }
}
