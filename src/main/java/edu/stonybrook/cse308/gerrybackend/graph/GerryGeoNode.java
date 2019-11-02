package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.ElectionType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class GerryGeoNode {

    protected final UUID id;
    @Getter
    protected final String name;
    protected DemographicData demographicData;
    protected ElectionData[] electionDataArr;
    protected Set<GerryEdge> adjacentEdges;
    @Getter
    protected final String geography;

    protected GerryGeoNode(UUID id, String name, DemographicData demographicData,
                           ElectionData[] electionDataArr, Set<GerryEdge> adjacentEdges, String geography) {
        this.id = id;
        this.name = name;
        this.demographicData = demographicData;
        this.electionDataArr = electionDataArr;
        this.adjacentEdges = adjacentEdges;
        this.geography = geography;
    }

    public String getId(){
        return this.id.toString();
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

    public ElectionData getElectionData(ElectionType electionId){
        return this.electionDataArr[electionId.value];
    }

    public void addEdge(GerryEdge edge) throws InvalidEdgeException {
        if (!edge.contains(this)){
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.add(edge);
    }
}
