package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PrecinctNode extends GerryNode<PrecinctEdge, DistrictNode> {

    @Getter
    @Column(name="county")
    private String county;

    public PrecinctNode(){
        super();
        this.county = "";
        this.parent = new DistrictNode(this);
    }

    public PrecinctNode(String id, String name,
                        DemographicData demographicData, ElectionData electionData,
                        Set<PrecinctEdge> adjacentEdges, String geography, String county, DistrictNode originalCD){
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.county = county;
        this.setOriginalDistrict(originalCD);
        this.parent = originalCD;
    }

    private void setOriginalDistrict(DistrictNode cluster){
        this.parent = cluster;
    }

    public DistrictNode getOriginalDistrict(){
        return this.parent;
    }

}
