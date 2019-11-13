package edu.stonybrook.cse308.gerrybackend.graph.edges;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import javax.persistence.Entity;

@Entity
public class DistrictEdge extends GerryEdge<DistrictNode> {

    public DistrictEdge(String id, DistrictNode node1, DistrictNode node2){
        super(id, node1, node2);
    }

}
