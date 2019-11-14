package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.AlgPhaseReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.reports.DemoBloc;
import edu.stonybrook.cse308.gerrybackend.data.reports.VoteBloc;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;

import java.util.Map;

public class PhaseZeroWorker extends AlgPhaseWorker<PhaseZeroReport> {

    private Map<String,DemoBloc> getDemoBlocs(){
        // TODO: fill in
        return null;
    }

    private Map<String,DemoBloc> getDemoBlocsHelper(DemographicType demoType, double threshold,
                                                    Map<String,DemographicData> precinctDemo){
        // TODO: fill in
        return null;
    }

    private Map<String,VoteBloc> getVoteBlocs(){
        // TODO: fill in
        return null;
    }

    private Map<String,VoteBloc> getVoteBlocsHelper(ElectionType electionType, double threshold,
                                                     Map<String, ElectionData> precinctVotes){
        // TODO: fill in
        return null;
    }

    @Override
    public PhaseZeroReport run(AlgPhaseInputs inputs) {
        return null;
    }

}
