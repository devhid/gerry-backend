package edu.stonybrook.cse308.gerrybackend.graph.edges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import javax.persistence.Entity;

@Entity
@JsonIgnoreProperties({"item1", "item2"})
public class StateEdge extends GerryEdge<StateNode> {

    public StateEdge(String id, StateNode node1, StateNode node2) {
        super(id, node1, node2);
    }

}
