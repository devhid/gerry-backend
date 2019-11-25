package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

import java.util.HashMap;
import java.util.Map;

public class PrecinctMove {

    private DistrictNode oldDistrict;
    private DistrictNode newDistrict;
    private PrecinctNode precinct;

    public Map<DistrictNode, DistrictNode> computeNewDistricts() {
        Map<DistrictNode, DistrictNode> oldToNewDistrictMap = new HashMap<>();
        /*
            TODO: fill in
            Considerations:
            - precinct move can add/remove edges to other districts
            - precinct move can split an old district
            - precinct move can create a split district
         */
        return oldToNewDistrictMap;
    }
}
