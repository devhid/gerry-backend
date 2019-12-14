package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PrecinctMove {

    @Getter
    private DistrictNode oldDistrict;

    @Getter
    private DistrictNode newDistrict;

    @Getter
    private PrecinctNode precinct;

    public Map<DistrictNode, DistrictNode> computeNewDistricts() throws MismatchedElectionException {
        Map<DistrictNode, DistrictNode> oldToNewDistrictMap = new HashMap<>();
        DistrictNode newOldDistrict = new DistrictNode(oldDistrict);
        DistrictNode newNewDistrict = new DistrictNode(newDistrict);
        newOldDistrict.removeBorderPrecinct(this.precinct);
        newNewDistrict.addBorderPrecinct(this.precinct);
        oldToNewDistrictMap.put(this.oldDistrict, newOldDistrict);
        oldToNewDistrictMap.put(this.newDistrict, newNewDistrict);
        return oldToNewDistrictMap;
    }
}
