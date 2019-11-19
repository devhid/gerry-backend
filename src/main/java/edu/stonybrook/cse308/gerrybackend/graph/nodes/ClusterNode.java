package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
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
    @ManyToMany(cascade=CascadeType.ALL)
    protected Set<C> children;

    @Getter
    @ElementCollection
    protected Set<String> counties;

    @Getter
    @Convert(converter=NodeTypeConverter.class)
    protected NodeType nodeType;

    protected ClusterNode(){
        super();
        this.children = null;
        this.counties = new HashSet<>();
        this.nodeType = NodeType.NOT_SET;
    }

    protected ClusterNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.nodeType = nodeType;
        this.children = null;
        this.counties = new HashSet<>();
    }

    protected ClusterNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography,
                          Set<C> children, Set<String> counties, StateNode parent) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.nodeType = nodeType;
        this.children = children;
        this.counties = counties;
        this.parent = parent;
    }

    int size(){
        return this.children.size();
    }

    protected abstract void loadAllCounties();

}
