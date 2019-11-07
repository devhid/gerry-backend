package edu.stonybrook.cse308.gerrybackend.graph.edges;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class PrecinctEdge extends GerryEdge<PrecinctNode> {

    public PrecinctEdge(UUID id, PrecinctNode node1, PrecinctNode node2){
        super(id, node1, node2);
    }

}
