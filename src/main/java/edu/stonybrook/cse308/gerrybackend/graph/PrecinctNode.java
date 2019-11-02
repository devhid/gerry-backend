package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.ElectionType;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

public class PrecinctNode extends GerryGeoNode {

    @Getter
    private String county;
    private ClusterNode[] originalCDMap;

    public PrecinctNode(UUID id, String name, DemographicData demographicData, ElectionData[] electionDataArr,
                        Set<GerryEdge> adjacentEdges, String geography, String county, ClusterNode[] originalCDMap) {
        super(id, name, demographicData, electionDataArr, adjacentEdges, geography);
        this.county = county;
        this.originalCDMap = originalCDMap;
    }

    public ClusterNode getOriginalCD(ElectionType electionId){
        return this.originalCDMap[electionId.value];
    }
}
