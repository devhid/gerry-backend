package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
public class PrecinctNode extends GerryGeoNode {

    @Getter
    @Column(name="county")
    private String county;

    public PrecinctNode(){
        super();
        this.county = "";
        this.parent = new DistrictNode();
    }

    public PrecinctNode(UUID id, String name, ElectionType electionId,
                        DemographicData demographicData, ElectionData electionData,
                        Set<GerryEdge> adjacentEdges, String geography, String county, DistrictNode originalCD){
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography);
        this.county = county;
        this.parent = originalCD;
    }

    private void setOriginalDistrict(DistrictNode cluster){
        this.parent = cluster;
    }

    public DistrictNode getOriginalDistrict(){
        return (DistrictNode) this.parent;
    }

}
