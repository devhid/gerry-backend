package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import java.util.*;

public abstract class ClusterNode<C extends GerryGeoNode, P extends ClusterNode> extends GerryGeoNode {

    @Getter
    @OneToMany(cascade= CascadeType.ALL, mappedBy="parent")
    protected Set<C> nodes;

    @Getter
    @ElementCollection
    protected Set<String> counties;

    public ClusterNode(){
        super();
        this.nodes = null;
        this.counties = new HashSet<>();
    }

    public ClusterNode(UUID id, String name, ElectionType electionId, DemographicData demographicData,
                       ElectionData electionData, Set<GerryEdge> adjacentEdges, String geography) {
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography);
        this.nodes = null;
        this.counties = new HashSet<>();
    }

    public ClusterNode(UUID id, String name, ElectionType electionId, DemographicData demographicData,
                       ElectionData electionData, Set<GerryEdge> adjacentEdges, String geography,
                       Set<C> nodes, Set<String> counties, P parent) {
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography);
        this.nodes = nodes;
        this.counties = counties;
        this.parent = parent;
    }

    public int getSize(){
        return this.nodes.size();
    }

    protected Set<C> getNodes(){
        return this.nodes;
    }
}
