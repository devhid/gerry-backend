package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.*;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@MappedSuperclass
@JsonIgnoreProperties({"adjacentNodes"})
@JsonTypeInfo(
        use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value=PrecinctNode.class, name="precinct"),
        @JsonSubTypes.Type(value=ClusterNode.class, name="cluster")
})
@JsonIdentityInfo(
        generator=ObjectIdGenerators.PropertyGenerator.class,
        property="id"
)
public abstract class GerryNode<E extends GerryEdge, P extends ClusterNode> {

    @Getter
    @Id
    @Column(name="id")
    protected String id;

    @Getter
    @NotNull
    @Column(name="name")
    protected String name;

    @Getter
    @NotNull
    @OneToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="node_demo_id")
    protected DemographicData demographicData;

    @Getter
    @NotNull
    @OneToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="node_election_id")
    protected ElectionData electionData;

    @Getter
    @OneToMany(cascade=CascadeType.ALL)     // one node has many edges
    protected Set<E> adjacentEdges;

    @Getter
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parent_id")
    protected P parent;

    @Getter
    @Column(name="geography")
    @JsonRawValue
    protected String geography;

    protected GerryNode(){
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.demographicData = new DemographicData();
        this.electionData = new ElectionData();
        this.adjacentEdges = new HashSet<>();
        this.geography = null;
    }

    protected GerryNode(String id, String name,
                        DemographicData demographicData, ElectionData electionData,
                        Set<E> adjacentEdges, String geography) {
        this.id = id.toString();
        this.name = name;
        this.demographicData = demographicData;
        this.electionData = electionData;
        this.adjacentEdges = adjacentEdges;
        this.geography = geography;
    }

    public Set<GerryNode> getAdjacentNodes() {
        Set<GerryNode> adjNodes = new HashSet<>();
        for (E edge : adjacentEdges){
            GerryNode adjNode = (GerryNode) ((edge.getItem1() == this) ? edge.getItem1() : edge.getItem2());
            adjNodes.add(adjNode);
        }
        return adjNodes;
    }

    public void addEdge(E edge) throws InvalidEdgeException {
        if (this.adjacentEdges.contains(edge)){
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.add(edge);
    }

    public ElectionType getElectionType(){
        return this.electionData.getElectionType();
    }
}