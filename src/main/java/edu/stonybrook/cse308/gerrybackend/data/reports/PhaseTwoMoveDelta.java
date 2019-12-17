package edu.stonybrook.cse308.gerrybackend.data.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PhaseTwoMoveDelta extends IterativeAlgPhaseDelta {

    @Getter
    private Map<String, DemographicData> newDemographicData;

    @Getter
    private Map<String, ElectionData> newElectionData;

    @Getter
    private String movedPrecinctId;

    @Getter
    @JsonIgnore
    private String oldDistrictId;

    @Getter
    private String newDistrictId;

    public PhaseTwoMoveDelta(int iteration, Map<String, DemographicData> newDemographicData,
                             Map<String, ElectionData> newElectionData, String movedPrecinctId, String oldDistrictId,
                             String newDistrictId) {
        super(iteration);
        this.newDemographicData = newDemographicData;
        this.newElectionData = newElectionData;
        this.movedPrecinctId = movedPrecinctId;
        this.oldDistrictId = oldDistrictId;
        this.newDistrictId = newDistrictId;
    }

    public static PhaseTwoMoveDelta fromPrecinctMove(PrecinctMove move, int iteration) {
        DistrictNode oldDistrict = move.getOldDistrict();
        DistrictNode newDistrict = move.getNewDistrict();
        PrecinctNode movedPrecinct = move.getPrecinct();
        Map<String, DemographicData> newDemographicData = new HashMap<>();
        Map<String, ElectionData> newElectionData = new HashMap<>();
        newDemographicData.put(oldDistrict.getNumericalId(), oldDistrict.getDemographicData());
        newDemographicData.put(newDistrict.getNumericalId(), newDistrict.getDemographicData());
        newElectionData.put(oldDistrict.getNumericalId(), oldDistrict.getElectionData());
        newElectionData.put(newDistrict.getNumericalId(), newDistrict.getElectionData());
        return new PhaseTwoMoveDelta(iteration, newDemographicData, newElectionData, movedPrecinct.getName(),
                oldDistrict.getNumericalId(), newDistrict.getNumericalId());
    }
}
