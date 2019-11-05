package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.converters.ElectionTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class GerryGeoNode {

    @Getter
    @Id
    @Column(name = "id")
    protected String id;

    @Getter
    @NotNull
    @Column(name = "name")
    protected String name;

    @Getter
    @NotNull
    @Convert(converter = ElectionTypeConverter.class)
    @Column(name = "election_id")
    protected ElectionType electionId;

    @Getter
    @NotNull
    @Column(name="demographic")
    protected DemographicData demographicData;

    @Getter
    @NotNull
    @Column(name="election")
    protected ElectionData electionData;

    @NotNull
    @ManyToMany     // one node has many edges, and one edge has 2 nodes
    protected Set<GerryEdge> adjacentEdges;

    @Getter
    @ManyToOne
    @JoinColumn(name="id")
    protected ClusterNode parent;

    @Getter
    @Column(name="geography")
    protected String geography;

    protected GerryGeoNode(){
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.electionId = ElectionType.getDefault();
        this.demographicData = new DemographicData();
        this.electionData = new ElectionData();
        this.adjacentEdges = new HashSet<>();
        this.geography = null;
    }

    protected GerryGeoNode(UUID id, String name, ElectionType electionId,
                           DemographicData demographicData, ElectionData electionData,
                           Set<GerryEdge> adjacentEdges, String geography) {
        this.id = id.toString();
        this.name = name;
        this.electionId = electionId;
        this.demographicData = demographicData;
        this.electionData = electionData;
        this.adjacentEdges = adjacentEdges;
        this.geography = geography;
    }

    public Set<GerryGeoNode> getAdjacentNodes() {
        Set<GerryGeoNode> adjNodes = new HashSet<>();
        for (GerryEdge edge : adjacentEdges){
            UnorderedPair<GerryGeoNode> edgeNodes = edge.getNodes();
            GerryGeoNode adjNode = (edgeNodes.getItem1() == this) ? edgeNodes.getItem2() : edgeNodes.getItem1();
            adjNodes.add(adjNode);
        }
        return adjNodes;
    }

    public void addEdge(GerryEdge edge) throws InvalidEdgeException {
        if (!edge.contains(this)){
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.add(edge);
    }
}
