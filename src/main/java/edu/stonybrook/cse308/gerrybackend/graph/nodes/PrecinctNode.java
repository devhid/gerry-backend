package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PrecinctNode extends GerryNode<PrecinctEdge, DistrictNode> {

    @Getter
    @Column(name="county")
    private String county;

    @Getter
    @Setter
    @Transient
    private DistrictNode userDistrict;

    public PrecinctNode(){
        super();
        this.county = "";
        this.parent = new DistrictNode(this);   // TODO: remove chaining creation after initial testing
    }

    public PrecinctNode(String id, String name,
                        DemographicData demographicData, ElectionData electionData,
                        Set<PrecinctEdge> adjacentEdges, String geography, String county, DistrictNode originalCD){
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.county = county;
        this.parent = originalCD;
    }

}
