package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.graph.Incumbent;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;
import lombok.Builder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class DistrictNode extends ClusterNode<DistrictEdge, PrecinctNode> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="incumbent_name")),
            @AttributeOverride(name="party", column=@Column(name="incumbent_party"))
    })
    private Incumbent incumbent;

    @Transient
    private Set<PrecinctNode> borderPrecincts;

    public DistrictNode(){
        super();
    }

    @Builder(builderClassName="DistrictNodeChildBuilder", builderMethodName="childBuilder")
    public DistrictNode(PrecinctNode child) {
        this();
        Set<PrecinctNode> precincts = new HashSet<>();
        precincts.add(child);
        this.children = precincts;
        this.demographicData = new DemographicData(child.demographicData);
        this.electionData = new ElectionData(child.electionData);
        this.counties.add(child.getCounty());
    }

    @Builder
    public DistrictNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                        ElectionData electionData, Set<DistrictEdge> adjacentEdges, String geography,
                        Set<PrecinctNode> precincts, Set<String> counties, StateNode state, Incumbent incumbent){
        super(id, name, nodeType, demographicData, electionData, adjacentEdges, geography, precincts, counties, state);
        this.incumbent = incumbent;
    }

    public DistrictNode(DistrictNode obj){
        this(UUID.randomUUID().toString(), obj.getName(), obj.nodeType, new DemographicData(obj.demographicData),
                new ElectionData(obj.electionData), new HashSet<>(obj.adjacentEdges), null, new HashSet<>(obj.children),
                new HashSet<>(obj.counties), obj.parent, obj.incumbent);
    }

    @Override
    protected void loadAllCounties() {
        this.counties = this.children.stream().map(PrecinctNode::getCounty).collect(Collectors.toSet());
    }

    public boolean isBorderPrecinct(PrecinctNode precinct){
        if (!(this.children.contains(precinct))){
            throw new IllegalArgumentException("Replace this string later!");
        }
        for (PrecinctNode adjPrecinct : GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class)){
            if (!(this.children.contains(adjPrecinct))){
                return true;
            }
        }
        return false;
    }

    private void computeBorderPrecincts(){
        this.borderPrecincts = new HashSet<>();
        this.children.forEach(p -> {
            if (this.isBorderPrecinct(p)){
                this.borderPrecincts.add(p);
            }
        });
    }

    @JsonIgnore
    public Set<PrecinctNode> getBorderPrecincts(){
        if (this.borderPrecincts == null){
            this.computeBorderPrecincts();
        }
        return this.borderPrecincts;
    }

    private void updateEdgeJoinability(){
        this.adjacentEdges.forEach(GerryEdge::computeNewJoinability);
    }

    /**
     * Adds a border precinct to this DistrictNode.
     * Designed as a helper for Phase II: Simulated Annealing.
     * @param precinct the border precinct selected for a move into this district
     * @throws MismatchedElectionException if the precinct's inner ElectionType does not match this DistrictNode's
     */
    public void addBorderPrecinct(PrecinctNode precinct) throws MismatchedElectionException {
        this.electionData = ElectionData.combine(this.electionData, precinct.getElectionData());
        this.demographicData = DemographicData.combine(this.demographicData, precinct.getDemographicData());
        if (this.borderPrecincts != null){
            this.borderPrecincts.add(precinct);
        }
        this.children.add(precinct);
        precinct.setParent(this);
        Set<DistrictNode> adjDistricts = GenericUtils.castSetOfObjects(this.getAdjacentNodes(), DistrictNode.class);
        Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class);
        Set<DistrictNode> newAdjDistricts = new HashSet<>();
        adjPrecincts.forEach(adjPrecinct -> {
            if (!(adjDistricts.contains(adjPrecinct.getParent()))){
                newAdjDistricts.add(adjPrecinct.getParent());
            }
        });
        newAdjDistricts.forEach(newAdjDistrict -> {
            DistrictEdge newEdge = new DistrictEdge(UUID.randomUUID().toString(), this, newAdjDistrict);
            try {
                this.addEdge(newEdge);
                newAdjDistrict.addEdge(newEdge);
            } catch (InvalidEdgeException e) {
                // should never happen
                e.printStackTrace();
            }
        });
        this.updateEdgeJoinability();
        this.markGeometriesStale();
    }

    /**
     * Removes a border precinct from this DistrictNode.
     * Designed as a helper for Phase II: Simulated Annealing.
     * @param precinct the border precinct selected to be removed from this DistrictNode
     * @throws MismatchedElectionException if the precinct passed does not have the same ElectionType as this District
     */
    public void removeBorderPrecinct(PrecinctNode precinct) throws MismatchedElectionException {
        this.electionData.subtract(precinct.getElectionData());
        this.demographicData.subtract(precinct.getDemographicData());
        if (this.borderPrecincts == null){
            this.computeBorderPrecincts();
        }
        if (this.borderPrecincts != null){
            this.borderPrecincts.remove(precinct);
        }
        this.children.remove(precinct);

        // Determine which DistrictNode objects are no longer adjacent after removing this border precinct.
        Set<DistrictNode> noLongerAdjDistricts = new HashSet<>();
        Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class);
        adjPrecincts.forEach(adjPrecinct -> {
            DistrictNode adjPrecinctParent = adjPrecinct.getParent();
            if (adjPrecinctParent != this){
                noLongerAdjDistricts.add(adjPrecinct.getParent());
            }
        });

        // Filter out the adjacent districts that are still tied to this district by a different border precinct.
        this.borderPrecincts.forEach(borderPrecinct -> {
            Set<PrecinctNode> adjToBorderPrecinctNodes = GenericUtils.castSetOfObjects(borderPrecinct.getAdjacentNodes(), PrecinctNode.class);
            adjToBorderPrecinctNodes.forEach(adjBorderPrecinct -> {
                if (noLongerAdjDistricts.contains(adjBorderPrecinct.getParent())){
                    noLongerAdjDistricts.remove(adjBorderPrecinct.getParent());
                }
            });
        });

        // Remove the old edges.
        Set<DistrictEdge> oldEdges = new HashSet<>();
        this.adjacentEdges.forEach(edge -> {
            DistrictNode otherDistrict = (edge.getItem1() == this) ? edge.getItem2() : edge.getItem1();
            if (noLongerAdjDistricts.contains(otherDistrict)){
                try {
                    otherDistrict.removeEdge(edge);
                    oldEdges.add(edge);
                } catch (InvalidEdgeException e) {
                    // should never happen
                    e.printStackTrace();
                }
            }
        });
        this.adjacentEdges.removeAll(oldEdges);
        this.updateEdgeJoinability();
        this.markGeometriesStale();
    }

    /**
     * Merges two disjoint DistrictNode objects (they do not share any internal nodes).
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
        DistrictNode biggerDistrict = (d1.size() > d2.size()) ? d1 : d2;
        DistrictNode smallerDistrict = (biggerDistrict == d1) ? d2 : d1;
        DistrictNode mergedDistrict = new DistrictNode(biggerDistrict);

        // Add all nodes and update counties.
        mergedDistrict.children.addAll(smallerDistrict.children);
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
