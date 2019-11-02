package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.ClusterType;
import edu.stonybrook.cse308.gerrybackend.exceptions.CircularReferenceException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import lombok.Getter;

import java.util.*;

public class ClusterNode extends GerryGeoNode implements Cloneable {

    @Getter
    private ClusterType clusterType;
    @Getter
    private Set<GerryGeoNode> nodes;
    @Getter
    private Set<String> touchedCounties;

    public ClusterNode(UUID id, String name, DemographicData demographicData, ElectionData[] electionDataArr,
                       Set<GerryEdge> adjacentEdges, String geography,
                       ClusterType clusterType, Set<GerryGeoNode> nodes, Set<String> touchedCounties) {
        super(id, name, demographicData, electionDataArr, adjacentEdges, geography);
        this.clusterType = clusterType;
        this.nodes = nodes;
        this.touchedCounties = touchedCounties;
    }

    public ClusterNode(UUID id, ClusterNode obj){
        super(id,
                obj.name,
                new DemographicData(obj.demographicData.getPopulationCopy(),
                        obj.demographicData.getVotingAgePopulationCopy()),
                Arrays.copyOf(obj.electionDataArr, obj.electionDataArr.length),
                new HashSet<>(obj.adjacentEdges), obj.geography);
        this.clusterType = obj.clusterType;
        this.nodes = new HashSet<>(obj.nodes);
        this.touchedCounties = new HashSet<>(obj.touchedCounties);
    }

    public int getSize(){
        return nodes.size();
    }

    public Set<PrecinctNode> getAllPrecincts(){
        Set<PrecinctNode> precincts = new HashSet<>();
        Set<ClusterNode> allNodes = new HashSet<>();
        for (GerryGeoNode node : this.nodes){
            if (node instanceof PrecinctNode){
                precincts.add((PrecinctNode) node);
            }
            else {
                allNodes.add((ClusterNode) node);
            }
        }
        for (ClusterNode node : allNodes){
            precincts.addAll(node.getAllPrecincts());
        }
        return precincts;
    }

    /**
     * Adds a set of disjoint (and potentially hitherto unrelated) nodes to this ClusterNode.
     *
     * This assumes there is no intersection in any of the geographical locations represented by the nodes.
     * Mainly used when constructing the GerryGraph object, not for Phase 1 or Phase 2 execution.
     *
     * @param nodes set of disjoint GerryGeoNode objects to add
     * @throws CircularReferenceException if nodes contains the instance object this method is called on
     * @throws MismatchedElectionException if there is an ElectionData object with a different election identifier
     */
    public void addNodes(Set<GerryGeoNode> nodes) throws CircularReferenceException, MismatchedElectionException {
        if (nodes == null){
            throw new IllegalArgumentException("Replace this string later!");
        }
        if (nodes.contains(this)){
            throw new CircularReferenceException("Replace this string later!");
        }

        // Add all nodes.
        this.nodes.addAll(nodes);
        Set<GerryEdge> allEdges = new HashSet<>(this.adjacentEdges);

        // Merge all DemographicData and ElectionData.
        DemographicData demoData = this.demographicData;
        ElectionData[] electionDataArr = this.electionDataArr;
        for (GerryGeoNode node: nodes){
            demoData = DemographicData.combine(demoData, node.demographicData);
            electionDataArr = ElectionData.combineElectionDataArr(electionDataArr, node.electionDataArr);
            if (node instanceof ClusterNode){
                this.touchedCounties.addAll(((ClusterNode) node).touchedCounties);
            }
            else {
                this.touchedCounties.add(((PrecinctNode) node).getCounty());
            }
            allEdges.addAll(node.adjacentEdges);
        }
        this.demographicData = demoData;
        this.electionDataArr = electionDataArr;

        // Filter edge references.
        Set<GerryEdge> externalAdjEdges = new HashSet<>();  // set of edges that connect to external, adj nodes
        Set<GerryGeoNode> externalAdjacentNodes = new HashSet<>();  // set of external, adj nodes
        for (GerryEdge edge : allEdges){
            UnorderedPair<GerryGeoNode> nodePair = edge.getNodes();
            boolean item1Found = this.nodes.contains(nodePair.getItem1());
            boolean item2Found = this.nodes.contains(nodePair.getItem2());
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
            GerryEdge newEdge = new GerryEdge(this, adjNode);   // should calculate joinability values
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
    public static ClusterNode combineClusters(ClusterNode c1, ClusterNode c2) throws MismatchedElectionException{
        UUID id = UUID.randomUUID();
        ClusterNode mergedCluster = (c1.getSize() > c2.getSize()) ? new ClusterNode(id, c1) : new ClusterNode(id, c2);

        // Add all nodes.
        mergedCluster.nodes.addAll(c2.nodes);

        // Update touchedCounties.
        mergedCluster.touchedCounties.addAll(c2.touchedCounties);

        // Merge DemographicData and ElectionData.
        mergedCluster.demographicData = DemographicData.combine(mergedCluster.demographicData, c2.demographicData);
        mergedCluster.electionDataArr = ElectionData.combineElectionDataArr(mergedCluster.electionDataArr, c2.electionDataArr);

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
        allAdjEdges.removeAll(connectingEdge);

        // Create new GerryEdge references to update internal references and joinability values.
        Map<GerryGeoNode, GerryEdge> adjNodesEdgeMap = new HashMap<>();
        for (GerryGeoNode adjNode : allAdjNodes){
            adjNodesEdgeMap.put(adjNode, new GerryEdge(mergedCluster, adjNode));    // recompute joinability
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
