package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.converters.NodeTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
@JsonTypeInfo(
        use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value=DistrictNode.class, name="district"),
        @JsonSubTypes.Type(value=StateNode.class, name="state")
})
public abstract class ClusterNode<E extends GerryEdge, C extends GerryNode> extends GerryNode<E,StateNode> {

    @Getter
    @OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
    protected Set<C> nodes;

    @Getter
    @ElementCollection
    protected Set<String> counties;

    @Getter
    @Convert(converter=NodeTypeConverter.class)
    protected NodeType nodeType;

    protected ClusterNode(){
        super();
        this.nodes = null;
        this.counties = new HashSet<>();
        this.nodeType = NodeType.NOT_SET;
    }

    protected ClusterNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.nodeType = nodeType;
        this.nodes = null;
        this.counties = new HashSet<>();
    }

    protected ClusterNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography,
                          Set<C> nodes, Set<String> counties, StateNode parent) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.nodeType = nodeType;
        this.nodes = nodes;
        this.counties = counties;
        this.parent = parent;
    }

    int getSize(){
        return this.nodes.size();
    }

}
