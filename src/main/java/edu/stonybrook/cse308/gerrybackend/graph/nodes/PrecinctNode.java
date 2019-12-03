package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@JsonIdentityReference(alwaysAsId = true)
public class PrecinctNode extends GerryNode<PrecinctEdge, DistrictNode> {

    @Getter
    @Column(name = "county")
    private String county;

    public PrecinctNode() {
        super();
        this.county = "";
    }

    @Builder
    public PrecinctNode(String id, String name,
                        DemographicData demographicData, ElectionData electionData,
                        Set<PrecinctEdge> adjacentEdges, String geography, String county, DistrictNode parent) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.county = county;
        this.parent = parent;
    }

}
