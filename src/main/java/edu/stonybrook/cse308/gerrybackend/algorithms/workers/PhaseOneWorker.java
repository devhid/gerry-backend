package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStopEnum;
import edu.stonybrook.cse308.gerrybackend.enums.types.PhaseOneType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import java.util.Set;

public class PhaseOneWorker extends AlgPhaseWorker<PhaseOneReport> {

    // TODO: remove
    private PhaseOneType phaseOneType;

    private Set<UnorderedPair<DistrictNode>> determineMajorityMinorityPairs(){
        return null;
    }

    private Set<UnorderedPair<DistrictNode>> determineOtherPairs(){
        return null;
    }

    private CandidatePairs determineCandidatePairs(){
        Set<UnorderedPair<DistrictNode>> majMinPairs = this.determineMajorityMinorityPairs();
        Set<UnorderedPair<DistrictNode>> otherPairs = this.determineOtherPairs();
        return new CandidatePairs(majMinPairs, otherPairs);
    }

    private boolean isLastIteration(CandidatePairs pairs){
        // TODO: fill in
        return false;
    }

    private void filterLastIterationPairs(boolean lastIteration, CandidatePairs pairs, PhaseOneStopEnum heuristic){
        // TODO: fill in
    }

    private PhaseOneMergeDelta joinCandidatePairs(CandidatePairs pairs){
        // TODO: fill in
        return null;
    }

    @Override
    public PhaseOneReport run(AlgPhaseInputs inputs) {
        return null;
    }
}
