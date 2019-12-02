package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PhaseTwoMoveDelta {

    @Getter
    private int iteration;

    @Getter
    private Map<String, DemographicData> newDemographicData;

    @Getter
    private Map<String, ElectionData> newElectionData;

    @Getter
    private String newDistrictId;

    @Getter
    private String movedPrecinctId;

    public PhaseTwoMoveDelta(int iteration, Map<String, DemographicData> newDemographicData,
                             Map<String, ElectionData> newElectionData, String newDistrictId, String movedPrecinctId) {
        this.iteration = iteration;
        this.newDemographicData = newDemographicData;
        this.newElectionData = newElectionData;
        this.newDistrictId = newDistrictId;
        this.movedPrecinctId = movedPrecinctId;
    }

    public static PhaseTwoMoveDelta fromPrecinctMove(PrecinctMove move, int iteration) {
        DistrictNode oldDistrict = move.getOldDistrict();
        DistrictNode newDistrict = move.getNewDistrict();
        PrecinctNode movedPrecinct = move.getPrecinct();
        Map<String, DemographicData> newDemographicData = new HashMap<>();
        Map<String, ElectionData> newElectionData = new HashMap<>();
        newDemographicData.put(oldDistrict.getId(), oldDistrict.getDemographicData());
        newDemographicData.put(newDistrict.getId(), newDistrict.getDemographicData());
        newElectionData.put(oldDistrict.getId(), oldDistrict.getElectionData());
        newElectionData.put(newDistrict.getId(), newDistrict.getElectionData());
        return new PhaseTwoMoveDelta(iteration, newDemographicData, newElectionData, newDistrict.getId(), movedPrecinct.getId());
    }
}
