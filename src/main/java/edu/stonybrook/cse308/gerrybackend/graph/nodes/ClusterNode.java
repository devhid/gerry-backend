package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.converters.NodeTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
public abstract class ClusterNode<E extends DistrictEdge, C extends GerryNode> extends GerryNode<E,StateNode> {

    @Getter
    @OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
    protected Set<C> nodes;

    @Getter
    @ElementCollection
    protected Set<String> counties;

    @Getter
    @Convert(converter=NodeTypeConverter.class)
    protected NodeType type;

    protected ClusterNode(){
        super();
        this.nodes = null;
        this.counties = new HashSet<>();
        this.type = NodeType.NOT_SET;
    }

    protected ClusterNode(UUID id, String name, NodeType type, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.type = type;
        this.nodes = null;
        this.counties = new HashSet<>();
    }

    protected ClusterNode(UUID id, String name, NodeType type, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography,
                          Set<C> nodes, Set<String> counties, StateNode parent) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.type = type;
        this.nodes = nodes;
        this.counties = counties;
        this.parent = parent;
    }

    int getSize(){
        return this.nodes.size();
    }

}
