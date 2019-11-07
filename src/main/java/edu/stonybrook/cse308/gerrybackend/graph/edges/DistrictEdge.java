package edu.stonybrook.cse308.gerrybackend.graph.edges;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class DistrictEdge extends GerryEdge<DistrictNode> {

    public DistrictEdge(UUID id, DistrictNode node1, DistrictNode node2){
        super(id, node1, node2);
    }

}
