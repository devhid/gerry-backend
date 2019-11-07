package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@MappedSuperclass
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
    @OneToOne(optional=false)
    @JoinColumn(name="node_demo_id")
    protected DemographicData demographicData;

    @Getter
    @NotNull
    @OneToOne(optional=false)
    @JoinColumn(name="node_election_id")
    protected ElectionData electionData;

    @ManyToMany     // one node has many edges, and one edge has 2 nodes
    @JoinTable(name="node_edges")
    protected Set<E> adjacentEdges;

    @Getter
    @ManyToOne
    @JoinColumn(name="parent_id")
    protected P parent;

    @Getter
    @Column(name="geography")
    protected String geography;

    protected GerryNode(){
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.demographicData = new DemographicData();
        this.electionData = new ElectionData();
        this.adjacentEdges = new HashSet<>();
        this.geography = null;
    }

    protected GerryNode(UUID id, String name,
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

    public <T> Set<T> getCastedAdjacentNodes(Class<T> clazz){
        Set<T> castedAdjNodes = new HashSet<>();
        for (E edge : adjacentEdges){
            @SuppressWarnings("unchecked")
            T castedAdjNode = (T) ((edge.getItem1() == this) ? edge.getItem1() : edge.getItem2());
            castedAdjNodes.add(castedAdjNode);
        }
        return castedAdjNodes;
    }

    public void addEdge(E edge) throws InvalidEdgeException {
        if (!edge.contains(this)){
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.add(edge);
    }

    public ElectionType getElectionId(){
        return this.electionData.getElectionId();
    }
}
