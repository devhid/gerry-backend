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

    /**
     * Merges two disjoint ClusterNode objects (they do not share any internal nodes).
     *
     * This is mainly used for Phase 1 execution.
     * @param d1 first DistrictNode object to merge
     * @param d2 second DistrictNode object to merge
     * @return merged DistrictNode
     */
    public static DistrictNode combine(DistrictNode d1, DistrictNode d2) throws MismatchedElectionException {
        if (!d1.getAdjacentNodes().contains(d2) || !d2.getAdjacentNodes().contains(d1)){
            throw new IllegalArgumentException("Replace this string later!");
        }
        DistrictNode biggerDistrict = (d1.getSize() > d2.getSize()) ? d1 : d2;
        DistrictNode smallerDistrict = (biggerDistrict == d1) ? d2 : d1;
        DistrictNode mergedDistrict = new DistrictNode(biggerDistrict);

        // Add all nodes and update counties.
        mergedDistrict.nodes.addAll(smallerDistrict.nodes);
        mergedDistrict.counties.addAll(smallerDistrict.counties);

        // Merge DemographicData and ElectionData.
        mergedDistrict.demographicData = DemographicData.combine(mergedDistrict.demographicData, smallerDistrict.demographicData);
        mergedDistrict.electionData = ElectionData.combine(mergedDistrict.electionData, smallerDistrict.electionData);

        // Get all adjacent nodes.
        Set<DistrictNode> allAdjNodes = GenericUtils.castSetOfObjects(d1.getAdjacentNodes(), DistrictNode.class);
        allAdjNodes.addAll(GenericUtils.castSetOfObjects(d2.getAdjacentNodes(), DistrictNode.class));
        allAdjNodes.remove(d1);     // from d2.getAdjacentNodes()
        allAdjNodes.remove(d2);     // from d1.getAdjacentNodes()

        // Get all adjacent edges (makes this easier to update external node edge references).
        Set<DistrictEdge> connectingEdge = new HashSet<>(d1.adjacentEdges);
        connectingEdge.retainAll(d2.adjacentEdges);     // should be only the edge connecting d1, d2
        Set<DistrictEdge> allAdjEdges = new HashSet<>(d1.adjacentEdges);
        allAdjEdges.addAll(d2.adjacentEdges);
        allAdjEdges.removeAll(connectingEdge);          // allAdjEdges = d1Adj U d2Adj - connectingEdge

        // Create new GerryEdge references to update internal references and joinability values.
        Map<DistrictNode, DistrictEdge> newAdjNodeEdges = new HashMap<>();
        for (DistrictNode adjNode : allAdjNodes){
            newAdjNodeEdges.put(adjNode, new DistrictEdge(UUID.randomUUID().toString(), mergedDistrict, adjNode));
        }

        // Fix each of the external, adjacent nodes' edges.
        newAdjNodeEdges.forEach((adjNode, newEdge) -> {
            Set<DistrictEdge> oldEdges = new HashSet<>(adjNode.adjacentEdges);
            oldEdges.retainAll(allAdjEdges);    // oldEdges = 1 or 2 edges (if adj to only 1 or to both d1,d2)
            adjNode.adjacentEdges.removeAll(oldEdges);
            adjNode.adjacentEdges.add(newEdge);
        });

        // Set the new merged cluster's adjacent edges.
        mergedDistrict.adjacentEdges = new HashSet<>(newAdjNodeEdges.values());
        return mergedDistrict;
    }
}
