package edu.stonybrook.cse308.gerrybackend.graph.edges;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

import javax.persistence.*;

@Entity
public class PrecinctEdge extends GerryEdge<PrecinctNode> {

    public PrecinctEdge(String id, PrecinctNode node1, PrecinctNode node2){
        super(id, node1, node2);
    }

}
