package edu.stonybrook.cse308.gerrybackend.graph.edges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.serializers.DistrictEdgeJsonDeserializer;

import javax.persistence.Entity;

@Entity
@JsonIgnoreProperties({"item1", "item2"})
@JsonDeserialize(using = DistrictEdgeJsonDeserializer.class)
public class DistrictEdge extends GerryEdge<DistrictNode> {

    public DistrictEdge() {
        super();
    }

    public DistrictEdge(String id) {
        super(id);
    }

    public DistrictEdge(String id, DistrictNode node1, DistrictNode node2) {
        super(id, node1, node2);
    }

}
