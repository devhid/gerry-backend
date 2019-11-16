package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.graph.Incumbent;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;

import javax.persistence.*;
import java.util.*;

@Entity
public class DistrictNode extends ClusterNode<DistrictEdge, PrecinctNode> {

    @ElementCollection
    private Set<Incumbent> incumbents;

    public DistrictNode(){
        super();
        this.setState(new StateNode(this)); // TODO: remove chaining creation after initial testing
        this.incumbents = new HashSet<>();
    }

    public DistrictNode(PrecinctNode child){
        this();
        Set<PrecinctNode> precincts = new HashSet<>();
        precincts.add(child);
        this.setPrecincts(precincts);
        this.demographicData = new DemographicData(child.demographicData);
        this.electionData = new ElectionData(child.electionData);
        this.counties.add(child.getCounty());
    }

    public DistrictNode(PrecinctNode child, StateNode state){
        super();
        this.setState(state);
        Set<PrecinctNode> precincts = new HashSet<>();
        precincts.add(child);
        this.setPrecincts(precincts);
        this.incumbents = new HashSet<>();
    }

    public DistrictNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                        ElectionData electionData, Set<DistrictEdge> adjacentEdges, String geography){
        super(id, name, nodeType, demographicData, electionData, adjacentEdges, geography);
        this.incumbents = new HashSet<>();
    }

    public DistrictNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                        ElectionData electionData, Set<DistrictEdge> adjacentEdges, String geography,
                        Set<PrecinctNode> precincts, Set<String> counties, StateNode state, Set<Incumbent> incumbents){
        super(id, name, nodeType, demographicData, electionData, adjacentEdges, geography, precincts, counties, state);
        this.incumbents = incumbents;
    }

    public DistrictNode(DistrictNode obj){
        this(UUID.randomUUID().toString(), obj.getName(), obj.nodeType, new DemographicData(obj.demographicData),
                new ElectionData(obj.electionData), new HashSet<>(obj.adjacentEdges), null, new HashSet<>(obj.nodes),
                new HashSet<>(obj.counties), obj.parent, new HashSet<>(obj.incumbents));
    }

    private void setPrecincts(Set<PrecinctNode> precincts){
        this.nodes = precincts;
    }

    private void setState(StateNode state){
        this.parent = state;
    }

//    /**
//     * Adds a set of PrecinctNodes to an empty DistrictNode.
//     *
//     * This assumes there is no intersection in any of the geographical locations represented by the nodes.
//     * Mainly used when constructing the StateNode object, not for Phase 1 or Phase 2 execution.
//     *
//     * @param precincts set of disjoint PrecinctNode objects to add
//     * @throws MismatchedElectionException if there is an ElectionData object with a different election identifier
//     */
//    public void addNodes(Set<PrecinctNode> precincts) throws MismatchedElectionException {
//        if (precincts == null){
//            throw new IllegalArgumentException("Replace this string later!");
//        }
//
//        // Add all nodes.
//        this.nodes.addAll(precincts);
//        Set<PrecinctGerryEdge> allEdges = new HashSet<PrecinctGerryEdge>();
//
//        // Merge all DemographicData and ElectionData.
//        DemographicData demoData = this.demographicData;
//        ElectionData electionData = this.electionData;
//        for (PrecinctNode precinct: precincts){
//            demoData = DemographicData.combine(demoData, precinct.demographicData);
//            electionData = ElectionData.combine(electionData, precinct.electionData);
//            this.counties.add(precinct.getCounty());
//            allEdges.addAll(precinct.adjacentEdges);
//        }
//        this.demographicData = demoData;
//        this.electionData = electionData;
//
//        // Filter edge references.
//        // Types of edges:
//        //      1) absorbed PrecinctNode and this DistrictNode
//        //      2) absorbed PrecinctNode and external GerryGeoNode
//        //      3) this DistrictNode and absorbed PrecinctNode
//        //      4) this DistrictNode and external GerryGeoNode
//        Set<PrecinctGerryEdge> externalAdjEdges = new HashSet<>();  // set of edges that connect to external, adj nodes
//        Set<GerryGeoNode> externalAdjacentNodes = new HashSet<>();  // set of external, adj nodes
//        for (PrecinctGerryEdge edge : allEdges){
//            GerryGeoNode item1 = edge.getItem1();
//            GerryGeoNode item2 = edge.getItem2();
//            boolean item1Found = this == item1;
//            boolean item2Found = this == item2;
//            if (!item1Found && item1 instanceof PrecinctNode){
//                item1Found = this.nodes.contains(item1);
//            }
//            if (!item2Found && item2 instanceof PrecinctNode){
//                item2Found = this.nodes.contains(item2);
//            }
//
//            if (item1Found ^ item2Found) {
//                if (item1Found) {
//                    externalAdjacentNodes.add(edge.getItem2());
//                }
//                else {
//                    externalAdjacentNodes.add(edge.getItem1());
//                }
//                externalAdjEdges.add(edge);
//            }
//        }
//
//        // Create new GerryEdge references to connect the new node with the external, adjacent nodes.
//        Set<PrecinctGerryEdge> newEdges = new HashSet<PrecinctGerryEdge>();
//        Map<GerryGeoNode, PrecinctGerryEdge> externalAdjEdgesMap = new HashMap<>();
//        for (GerryGeoNode adjNode : externalAdjacentNodes){
//            // calculate joinability values on edge creation
//            PrecinctGerryEdge newEdge = new PrecinctGerryEdge(UUID.randomUUID(), this, adjNode);
//            newEdges.add(newEdge);
//            externalAdjEdgesMap.put(adjNode, newEdge);
//        }
//
//        // Fix each of the external, adjacent nodes' edges.
//        externalAdjEdgesMap.forEach((adjNode, newEdge) -> {
//            Set<PrecinctGerryEdge> oldEdges = new HashSet<PrecinctGerryEdge>(adjNode.adjacentEdges);
//            oldEdges.retainAll(externalAdjEdges);   // set intersection
//            adjNode.adjacentEdges.removeAll(oldEdges);
//            adjNode.adjacentEdges.add(newEdge);
//        });
//
//        // Set this cluster's new adjacent edges.
//        this.adjacentEdges = newEdges;
//    }

    /**
     * Merges two disjoint ClusterNode objects (they do not share any internal nodes).
     *
     * This is mainly used for Phase 1 execution.
     * @param c1 first ClusterNode object to merge
     * @param c2 second ClusterNode object to merge
     * @return merged ClusterNode
     */
    public static DistrictNode combine(DistrictNode c1, DistrictNode c2) throws MismatchedElectionException {
        if (!c1.getAdjacentNodes().contains(c2) || !c2.getAdjacentNodes().contains(c1)){
            throw new IllegalArgumentException("Replace this string later!");
        }
        DistrictNode mergedCluster = (c1.getSize() > c2.getSize()) ? new DistrictNode(c1) : new DistrictNode(c2);

        // Add all nodes and update counties.
        mergedCluster.nodes.addAll(c2.nodes);
        mergedCluster.counties.addAll(c2.counties);

        // Merge DemographicData and ElectionData.
        mergedCluster.demographicData = DemographicData.combine(mergedCluster.demographicData, c2.demographicData);
        mergedCluster.electionData = ElectionData.combine(mergedCluster.electionData, c2.electionData);

        // Get all adjacent nodes.
        Set<DistrictNode> allAdjNodes = GenericUtils.castSetOfObjects(c1.getAdjacentNodes(), DistrictNode.class);
        allAdjNodes.addAll(GenericUtils.castSetOfObjects(c2.getAdjacentNodes(), DistrictNode.class));
        allAdjNodes.remove(c1);     // from c2.getAdjacentNodes()
        allAdjNodes.remove(c2);     // from c1.getAdjacentNodes()

        // Get all adjacent edges (makes this easier to update external node edge references)
        Set<DistrictEdge> connectingEdge = new HashSet<>(c1.adjacentEdges);
        connectingEdge.retainAll(c2.adjacentEdges);     // should be only the edge connecting c1, c2
        Set<DistrictEdge> allAdjEdges = new HashSet<>(c1.adjacentEdges);
        allAdjEdges.addAll(c2.adjacentEdges);
        allAdjEdges.removeAll(connectingEdge);          // allAdjEdges = c1Adj U c2Adj - connectingEdge

        // Create new GerryEdge references to update internal references and joinability values.
        Map<DistrictNode, DistrictEdge> adjNodesEdgeMap = new HashMap<>();
        for (DistrictNode adjNode : allAdjNodes){
            adjNodesEdgeMap.put(adjNode, new DistrictEdge(UUID.randomUUID().toString(), mergedCluster, adjNode));
        }

        // Fix each of the external, adjacent nodes' edges.
        adjNodesEdgeMap.forEach((adjNode, newEdge) -> {
            Set<DistrictEdge> oldEdges = new HashSet<>(adjNode.adjacentEdges);
            oldEdges.retainAll(allAdjEdges);
            adjNode.adjacentEdges.removeAll(oldEdges);
            adjNode.adjacentEdges.add(newEdge);
        });

        // Set the new merged cluster's adjacent edges.
        mergedCluster.adjacentEdges = new HashSet<>(adjNodesEdgeMap.values());
        return mergedCluster;
    }
}
