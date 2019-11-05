package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.converters.DistrictTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.DistrictType;
import edu.stonybrook.cse308.gerrybackend.exceptions.CircularReferenceException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;

@Entity
@AttributeOverride(name="nodes", column=@Column(name="precincts"))
public class DistrictNode extends ClusterNode<PrecinctNode, StateNode> {

    @Getter
    @Convert(converter=DistrictTypeConverter.class)
    private DistrictType type;

    public DistrictNode(){
        super();
        this.type = DistrictType.ORIGINAL_DISTRICT;
        this.setState(new StateNode());
    }

    public DistrictNode(UUID id, String name, DistrictType type, ElectionType electionId, DemographicData demographicData,
                        ElectionData electionData, Set<GerryEdge> adjacentEdges, String geography){
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography);
        this.type = type;
    }

    public DistrictNode(UUID id, String name, DistrictType type, ElectionType electionId, DemographicData demographicData,
                        ElectionData electionData, Set<GerryEdge> adjacentEdges, String geography,
                        Set<PrecinctNode> precincts, Set<String> counties, StateNode state){
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography, precincts, counties, state);
        this.type = type;
    }

    public DistrictNode(UUID id, DistrictNode obj){
        this(id, obj.getName(), DistrictType.ORIGINAL_DISTRICT, obj.electionId,
                new DemographicData(UUID.randomUUID(), obj.demographicData.getPopulationCopy(),
                        obj.demographicData.getVotingAgePopulationCopy()),
                new ElectionData(UUID.randomUUID(), obj.electionData.getElectionId(), obj.electionData.getVotesCopy(),
                        obj.electionData.getWinner()),
                new HashSet<>(obj.adjacentEdges), obj.geography);
        this.type = obj.type;
        this.nodes = new HashSet<>(obj.nodes);
        this.counties = new HashSet<>(obj.counties);
        this.setState((StateNode) obj.parent);
    }

    private void setPrecincts(Set<PrecinctNode> precincts){
        this.nodes = precincts;
    }

    public Set<PrecinctNode> getPrecincts(){
        return this.getNodes();
    }

    private void setState(StateNode state){
        this.parent = state;
    }

    public StateNode getState(){
        return (StateNode) this.parent;
    }

    /**
     * Adds a set of PrecinctNodes to this ClusterNode.
     *
     * This assumes there is no intersection in any of the geographical locations represented by the nodes.
     * Mainly used when constructing the GerryGraph object, not for Phase 1 or Phase 2 execution.
     *
     * @param precincts set of disjoint PrecinctNode objects to add
     * @throws CircularReferenceException if nodes contains the instance object this method is called on
     * @throws MismatchedElectionException if there is an ElectionData object with a different election identifier
     */
    public void addNodes(Set<PrecinctNode> precincts) throws MismatchedElectionException {
        if (precincts == null){
            throw new IllegalArgumentException("Replace this string later!");
        }

        // Add all nodes.
        this.nodes.addAll(precincts);
        Set<GerryEdge> allEdges = new HashSet<>(this.adjacentEdges);

        // Merge all DemographicData and ElectionData.
        DemographicData demoData = this.demographicData;
        ElectionData electionData = this.electionData;
        for (PrecinctNode precinct: precincts){
            demoData = DemographicData.combine(demoData, precinct.demographicData);
            electionData = ElectionData.combine(electionData, precinct.electionData);
            this.counties.add(precinct.getCounty());
            allEdges.addAll(precinct.adjacentEdges);
        }
        this.demographicData = demoData;
        this.electionData = electionData;

        // Filter edge references.
        // Types of edges:
        //      1) absorbed PrecinctNode and this ClusterNode
        //      2) absorbed PrecinctNode and external GerryGeoNode
        //      3) this ClusterNode and absorbed PrecinctNode
        //      4) this ClusterNode and external GerryGeoNode
        Set<GerryEdge> externalAdjEdges = new HashSet<>();  // set of edges that connect to external, adj nodes
        Set<GerryGeoNode> externalAdjacentNodes = new HashSet<>();  // set of external, adj nodes
        for (GerryEdge edge : allEdges){
            UnorderedPair<GerryGeoNode> nodePair = edge.getNodes();

            GerryGeoNode item1 = nodePair.getItem1();
            GerryGeoNode item2 = nodePair.getItem2();
            boolean item1Found = this == item1;
            boolean item2Found = this == item2;
            if (!item1Found && item1 instanceof PrecinctNode){
                item1Found = this.nodes.contains(item1);
            }
            if (!item2Found && item2 instanceof PrecinctNode){
                item2Found = this.nodes.contains(item2);
            }

            if (item1Found ^ item2Found) {
                if (item1Found) {
                    externalAdjacentNodes.add(nodePair.getItem2());
                }
                else {
                    externalAdjacentNodes.add(nodePair.getItem1());
                }
                externalAdjEdges.add(edge);
            }
        }

        // Create new GerryEdge references to connect the new node with the external, adjacent nodes.
        Set<GerryEdge> newEdges = new HashSet<GerryEdge>();
        Map<GerryGeoNode, GerryEdge> externalAdjEdgesMap = new HashMap<>();
        for (GerryGeoNode adjNode : externalAdjacentNodes){
            // calculate joinability values on edge creation
            GerryEdge newEdge = new GerryEdge(UUID.randomUUID(), this, adjNode);
            newEdges.add(newEdge);
            externalAdjEdgesMap.put(adjNode, newEdge);
        }

        // Fix each of the external, adjacent nodes' edges.
        externalAdjEdgesMap.forEach((adjNode, newEdge) -> {
            Set<GerryEdge> oldEdges = new HashSet<>(adjNode.adjacentEdges);
            oldEdges.retainAll(externalAdjEdges);   // set intersection
            adjNode.adjacentEdges.removeAll(oldEdges);
            adjNode.adjacentEdges.add(newEdge);
        });

        // Set this cluster's new adjacent edges.
        this.adjacentEdges = newEdges;
    }

    /**
     * Merges two disjoint ClusterNode objects (they do not share any internal nodes).
     *
     * This is mainly used for Phase 1 execution.
     * @param c1 first ClusterNode object to merge
     * @param c2 second ClusterNode object to merge
     * @return merged ClusterNode
     */
    public static DistrictNode combineClusters(DistrictNode c1, DistrictNode c2) throws MismatchedElectionException {
        if (!c1.getAdjacentNodes().contains(c2) || !c2.getAdjacentNodes().contains(c1)){
            throw new IllegalArgumentException("Replace this string later!");
        }
        UUID id = UUID.randomUUID();
        DistrictNode mergedCluster = (c1.getSize() > c2.getSize()) ? new DistrictNode(id, c1) : new DistrictNode(id, c2);

        // Add all nodes.
        mergedCluster.nodes.addAll(c2.nodes);

        // Update counties.
        mergedCluster.counties.addAll(c2.counties);

        // Merge DemographicData and ElectionData.
        mergedCluster.demographicData = DemographicData.combine(mergedCluster.demographicData, c2.demographicData);
        mergedCluster.electionData = ElectionData.combine(mergedCluster.electionData, c2.electionData);

        // Get all adjacent nodes.
        Set<GerryGeoNode> allAdjNodes = c1.getAdjacentNodes();    // contains c2
        allAdjNodes.addAll(c2.getAdjacentNodes());    // adjNodes1 = (adjNodes1 U adjNodes2)
        allAdjNodes.remove(c1);
        allAdjNodes.remove(c2);     // from c2.getAdjacentNodes()

        // Get all adjacent edges (makes this easier to update external node edge references)
        Set<GerryEdge> connectingEdge = new HashSet<>(c1.adjacentEdges);
        connectingEdge.retainAll(c2.adjacentEdges);     // should be only the edge connecting c1, c2
        Set<GerryEdge> allAdjEdges = new HashSet<>(c1.adjacentEdges);
        allAdjEdges.addAll(c2.adjacentEdges);
        allAdjEdges.removeAll(connectingEdge);          // allAdjEdges = c1Adj U c2Adj - connectingEdge

        // Create new GerryEdge references to update internal references and joinability values.
        Map<GerryGeoNode, GerryEdge> adjNodesEdgeMap = new HashMap<>();
        for (GerryGeoNode adjNode : allAdjNodes){
            // recompute joinability on edge creation
            adjNodesEdgeMap.put(adjNode, new GerryEdge(UUID.randomUUID(), mergedCluster, adjNode));
        }

        // Fix each of the external, adjacent nodes' edges.
        adjNodesEdgeMap.forEach((adjNode, newEdge) -> {
            Set<GerryEdge> oldEdges = new HashSet<>(adjNode.adjacentEdges);
            oldEdges.retainAll(allAdjEdges);
            adjNode.adjacentEdges.removeAll(oldEdges);
            adjNode.adjacentEdges.add(newEdge);
        });

        // Set the new merged cluster's adjacent edges.
        mergedCluster.adjacentEdges = new HashSet<>(adjNodesEdgeMap.values());
        return mergedCluster;
    }
}
