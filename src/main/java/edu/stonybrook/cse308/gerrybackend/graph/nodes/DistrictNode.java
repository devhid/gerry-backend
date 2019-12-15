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
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Named;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NamedEntityGraph(
        name = "district-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("nodeType"),
                @NamedAttributeNode("demographicData"),
                @NamedAttributeNode("electionData"),
                @NamedAttributeNode("adjacentEdges"),
                @NamedAttributeNode("children"),
                @NamedAttributeNode("counties")
        }
)
@Entity
public class DistrictNode extends ClusterNode<DistrictEdge, PrecinctNode> {

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "incumbent_name")),
            @AttributeOverride(name = "party", column = @Column(name = "incumbent_party"))
    })
    private Incumbent incumbent;

    @Transient
    private Set<PrecinctNode> borderPrecincts;

    @Getter
    @Setter
    @Column(name = "numerical_id")
    private String numericalId; // for phase 1 to map colors properly

    public DistrictNode() {
        super();
    }

    @Builder(builderClassName = "DistrictNodeChildBuilder", builderMethodName = "childBuilder")
    public DistrictNode(PrecinctNode child, NodeType nodeType) {
        this();
        Set<PrecinctNode> precincts = new HashSet<>();
        precincts.add(child);
        this.children = precincts;
        this.demographicData = new DemographicData(child.demographicData);
        this.electionData = new ElectionData(child.electionData);
        this.counties.add(child.getCounty());
        this.nodeType = nodeType;
    }

    @Builder
    public DistrictNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                        ElectionData electionData, Set<DistrictEdge> adjacentEdges, String geography,
                        Set<PrecinctNode> precincts, Set<String> counties, StateNode state, Incumbent incumbent) {
        super(id, name, nodeType, demographicData, electionData, adjacentEdges, geography, precincts, counties, state);
        this.incumbent = incumbent;
    }

    public DistrictNode(DistrictNode obj) {
        this(UUID.randomUUID().toString(), obj.getName(), obj.nodeType, new DemographicData(obj.demographicData),
                new ElectionData(obj.electionData), obj.getAdjacentEdgesCopy(), null, new HashSet<>(obj.children),
                new HashSet<>(obj.counties), obj.parent, obj.incumbent);
        this.adjacentEdges.forEach(e -> {
            DistrictNode originalNode = (e.getItem1() == obj) ? e.getItem1() : e.getItem2();
            e.remove(originalNode);
            e.add(this);
        });
        this.numericalId = obj.getNumericalId();
    }

    @Override
    protected void aggregateCounties() {
        this.counties = this.children.stream().map(PrecinctNode::getCounty).collect(Collectors.toSet());
    }

    public boolean isBorderPrecinct(PrecinctNode precinct) {
        if (!(this.children.contains(precinct))) {
            throw new IllegalArgumentException("Replace this string later!");
        }
        for (PrecinctNode adjPrecinct : GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class)) {
            if (!(this.children.contains(adjPrecinct))) {
                return true;
            }
        }
        return false;
    }

    private void computeBorderPrecincts() {
        this.borderPrecincts = new HashSet<>();
        this.children.forEach(p -> {
            if (this.isBorderPrecinct(p)) {
                this.borderPrecincts.add(p);
            }
        });
    }

    @JsonIgnore
    public Set<PrecinctNode> getBorderPrecincts() {
        if (this.borderPrecincts == null) {
            this.computeBorderPrecincts();
        }
        return this.borderPrecincts;
    }

    private void updateEdgeJoinability() {
        this.adjacentEdges.forEach(GerryEdge::computeNewJoinability);
    }

    /**
     * Adds a border precinct to this DistrictNode.
     * Designed as a helper for Phase II: Simulated Annealing.
     *
     * @param precinct the border precinct selected for a move into this district
     * @throws MismatchedElectionException if the precinct's inner ElectionType does not match this DistrictNode's
     */
    public void addBorderPrecinct(PrecinctNode precinct) throws MismatchedElectionException {
        this.electionData = ElectionData.combine(this.electionData, precinct.getElectionData());
        this.demographicData = DemographicData.combine(this.demographicData, precinct.getDemographicData());
        if (this.borderPrecincts != null) {
            this.borderPrecincts.add(precinct);
        }
        this.children.add(precinct);
        precinct.setParent(this);
        Set<DistrictNode> adjDistricts = GenericUtils.castSetOfObjects(this.getAdjacentNodes(), DistrictNode.class);
        Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class);
        Set<DistrictNode> newAdjDistricts = new HashSet<>();
        adjPrecincts.forEach(adjPrecinct -> {
            if (!(adjDistricts.contains(adjPrecinct.getParent()))) {
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
     *
     * @param precinct the border precinct selected to be removed from this DistrictNode
     * @throws MismatchedElectionException if the precinct passed does not have the same ElectionType as this District
     */
    public void removeBorderPrecinct(PrecinctNode precinct) throws MismatchedElectionException {
        this.electionData.subtract(precinct.getElectionData());
        this.demographicData.subtract(precinct.getDemographicData());
        if (this.borderPrecincts == null) {
            this.computeBorderPrecincts();
        }
        if (this.borderPrecincts != null) {
            this.borderPrecincts.remove(precinct);
        }
        this.children.remove(precinct);

        // Determine which DistrictNode objects are no longer adjacent after removing this border precinct.
        Set<DistrictNode> noLongerAdjDistricts = new HashSet<>();
        Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(precinct.getAdjacentNodes(), PrecinctNode.class);
        adjPrecincts.forEach(adjPrecinct -> {
            DistrictNode adjPrecinctParent = adjPrecinct.getParent();
            if (adjPrecinctParent != this) {
                noLongerAdjDistricts.add(adjPrecinct.getParent());
            }
        });

        // Filter out the adjacent districts that are still tied to this district by a different border precinct.
        this.borderPrecincts.forEach(borderPrecinct -> {
            Set<PrecinctNode> adjToBorderPrecinctNodes = GenericUtils.castSetOfObjects(borderPrecinct.getAdjacentNodes(), PrecinctNode.class);
            adjToBorderPrecinctNodes.forEach(adjBorderPrecinct -> {
                if (noLongerAdjDistricts.contains(adjBorderPrecinct.getParent())) {
                    noLongerAdjDistricts.remove(adjBorderPrecinct.getParent());
                }
            });
        });

        // Remove the old edges.
        Set<DistrictEdge> oldEdges = new HashSet<>();
        this.adjacentEdges.forEach(edge -> {
            DistrictNode otherDistrict = (edge.getItem1() == this) ? edge.getItem2() : edge.getItem1();
            if (noLongerAdjDistricts.contains(otherDistrict)) {
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

    public static DistrictNode combineForStatisticsOnly(DistrictNode d1, DistrictNode d2) throws MismatchedElectionException {
        return DistrictNode.combine(d1, d2, false, null);
    }

    /**
     * Merges two disjoint DistrictNode objects (they do not share any internal nodes).
     * <p>
     * This is mainly used for Phase 1 execution. Note that this is only intended to be used for DistrictNodes NOT
     * persisted to the backing database, hence we don't have to worry about orphan removal and the like.
     *
     * @param d1          first DistrictNode object to merge
     * @param d2          second DistrictNode object to merge
     * @param updateEdges whether to update external node edges
     * @return merged DistrictNode
     */
    public static DistrictNode combine(DistrictNode d1, DistrictNode d2, boolean updateEdges,
                                       Set<DistrictNode> remnantDistricts)
            throws MismatchedElectionException {
        if (!d1.isAdjacentTo(d2) || !d2.isAdjacentTo(d1)) {
            throw new IllegalArgumentException("Replace this string later!");
        }
        DistrictNode biggerDistrict = (d1.size() > d2.size()) ? d1 : d2;
        DistrictNode smallerDistrict = (biggerDistrict == d1) ? d2 : d1;
        DistrictNode mergedDistrict = (updateEdges && remnantDistricts != null) ? biggerDistrict : new DistrictNode(biggerDistrict);

        // Add all nodes and update counties.
        mergedDistrict.children.addAll(smallerDistrict.children);
        mergedDistrict.counties.addAll(smallerDistrict.counties);

        // Merge DemographicData and ElectionData.
        mergedDistrict.demographicData.add(smallerDistrict.demographicData);
        mergedDistrict.electionData.add(smallerDistrict.electionData);

        if (updateEdges) {
            // Get all adjacent edges (makes it easier to update external node edge references).
            Set<DistrictEdge> connectingEdge = new HashSet<>(d1.adjacentEdges);
            connectingEdge.retainAll(d2.adjacentEdges);     // should be only the edge connecting d1, d2
            mergedDistrict.adjacentEdges.removeAll(connectingEdge);
            smallerDistrict.adjacentEdges.removeAll(connectingEdge);
            mergedDistrict.adjacentEdges.forEach(GerryEdge::clearJoinability);     // mark stale

            smallerDistrict.adjacentEdges.forEach(adjEdge -> {
                adjEdge.remove(smallerDistrict);
                adjEdge.add(mergedDistrict);
            });
            mergedDistrict.adjacentEdges.addAll(smallerDistrict.adjacentEdges);

            smallerDistrict.clearEdges();
            remnantDistricts.add(smallerDistrict);          // mark as remnant to be deleted later
        }

        return mergedDistrict;
    }
}
