package edu.stonybrook.cse308.gerrybackend.graph.edges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.serializers.PrecinctEdgeJsonDeserializer;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@JsonIgnoreProperties({"item1", "item2"})
@JsonDeserialize(using = PrecinctEdgeJsonDeserializer.class)
public class PrecinctEdge extends GerryEdge<PrecinctNode> {

    public PrecinctEdge() {
        super();
    }

    public PrecinctEdge(String id) {
        super(id);
    }

    public PrecinctEdge(String id, PrecinctNode node1, PrecinctNode node2) {
        super(id, node1, node2);
    }

}
