package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.Joinability;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import lombok.Getter;

public class GerryEdge {

    @Getter
    private UnorderedPair<GerryGeoNode> nodes;
    private Joinability joinability;

    public GerryEdge(GerryGeoNode node1, GerryGeoNode node2){
        this.nodes = new UnorderedPair<>(node1, node2);
        this.joinability = new Joinability(node1, node2);
    }

    public boolean contains(GerryGeoNode node){
        return nodes.contains(node);
    }
}
