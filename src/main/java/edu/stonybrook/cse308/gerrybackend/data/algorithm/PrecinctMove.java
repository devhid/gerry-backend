package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PrecinctMove {

    @Getter
    private DistrictNode oldDistrict;

    @Getter
    private DistrictNode newDistrict;

    @Getter
    private PrecinctNode precinct;

    @JsonIgnore
    private Map<DistrictNode, DistrictNode> newDistricts;

    public PrecinctMove(DistrictNode oldDistrict, DistrictNode newDistrict, PrecinctNode precinct) {
        this.oldDistrict = oldDistrict;
        this.newDistrict = newDistrict;
        this.precinct = precinct;
    }

    private void computeNewDistricts() throws MismatchedElectionException {
        Map<DistrictNode, DistrictNode> oldToNewDistrictMap = new HashMap<>();
        DistrictNode newOldDistrict = new DistrictNode(oldDistrict);
        DistrictNode newNewDistrict = new DistrictNode(newDistrict);
        newOldDistrict.removeBorderPrecinct(this.precinct, false);
        newNewDistrict.addBorderPrecinct(this.precinct, false);
        oldToNewDistrictMap.put(this.oldDistrict, newOldDistrict);
        oldToNewDistrictMap.put(this.newDistrict, newNewDistrict);
        this.newDistricts = oldToNewDistrictMap;
    }

    public Map<DistrictNode, DistrictNode> getNewDistricts() throws MismatchedElectionException {
        if (this.newDistricts == null) {
            this.computeNewDistricts();
        }
        return this.newDistricts;
    }
}
