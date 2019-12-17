package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.StateEdge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NamedEntityGraph(
        name = "state-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("nodeType"),
                @NamedAttributeNode("demographicData"),
                @NamedAttributeNode("electionData"),
                @NamedAttributeNode(value = "children", subgraph = "children-subgraph"),
                @NamedAttributeNode("counties")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "children-subgraph",
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
        }
)
@Entity
public class StateNode extends ClusterNode<StateEdge, DistrictNode> {

    @Getter
    private StateType stateType;

    @Getter
    @Lob
    @Column(name = "redistricting_legislation", columnDefinition = "TEXT")
    private String redistrictingLegislation;

    @Transient
    @Setter
    @JsonIgnore
    private Map<DistrictNode, DistrictNode> proposedNewDistricts;

    public StateNode() {
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.stateType = StateType.NOT_SET;
    }

    @Builder
    public StateNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                     ElectionData electionData, String geography, Set<DistrictNode> districts, Set<String> counties,
                     StateType stateType, String redistrictingLegislation) {
        super(id, name, nodeType, demographicData, electionData, null, geography, districts, counties, null);
        this.stateType = stateType;
        this.redistrictingLegislation = redistrictingLegislation;
    }

    @Override
    protected void aggregateCounties() {
        this.children.forEach(DistrictNode::aggregateCounties);
        this.counties = this.children.stream().flatMap(d -> d.getCounties().stream()).collect(Collectors.toSet());
    }

    public void remapDistrictReferences(Map<DistrictNode, DistrictNode> changedDistricts) {
        this.children = this.children.stream()
                .map(d -> changedDistricts.getOrDefault(d, d))
                .collect(Collectors.toSet());
    }

    public Set<DistrictNode> getProposedDistricts() {
        return this.children.stream()
                .map(d -> this.proposedNewDistricts.getOrDefault(d, d))
                .collect(Collectors.toSet());
    }

    /**
     * Fills in all properties annotated with @Transient in the graph represented by this StateNode.
     * It calls:
     * - fillInPrecinctUserDistrictReferences
     * - fillInEdgeNodeReferences
     */
    public void fillInTransientProperties() {
        this.fillInParentReferences();
        this.fillInEdgeNodeReferences();
    }

    /**
     * Fills in all nodes' parent references after the StateNode has been deserialized.
     * <p>
     * As parent references are not stored in the DB, these wil have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    public void fillInParentReferences() {
//         TODO: is this check needed?
//        if (this.nodeType != NodeType.USER) {
//            return;
//        }
        Set<DistrictNode> districts = this.getChildren();
        districts.forEach(d -> {
            d.setParent(this);
            d.getChildren().forEach(p -> {
                p.setParent(d);
            });
        });
    }

    public static void fillAdjacentNodes(Map<String, UnorderedPair<GerryNode>> edgeMap,
                                         Set<? extends GerryNode> nodes) {
        for (GerryNode node : nodes) {
            Set<GerryEdge> nodeEdges = node.getAdjacentEdges();
            for (GerryEdge nodeEdge : nodeEdges) {
                if (!edgeMap.containsKey(nodeEdge.getId())) {
                    UnorderedPair<GerryNode> edgeNodes = new UnorderedPair<>();
                    edgeNodes.add(node);
                    edgeMap.put(nodeEdge.getId(), edgeNodes);
                } else {
                    edgeMap.get(nodeEdge.getId()).add(node);
                }
            }
        }
    }

    public static void fillDistrictEdgeReferences(Map<String, UnorderedPair<GerryNode>> edgeMap,
                                                  Set<DistrictNode> nodes) {
        for (DistrictNode n : nodes) {
            for (DistrictEdge e : n.getAdjacentEdges()) {
                if (e.size() == 0) {
                    UnorderedPair<GerryNode> pair = edgeMap.get(e.getId());
                    e.add((DistrictNode) pair.getItem1());
                    e.add((DistrictNode) pair.getItem2());
                }
            }
        }
    }

    public static void fillPrecinctEdgeReferences(Map<String, UnorderedPair<GerryNode>> edgeMap,
                                                  Set<PrecinctNode> nodes) {
        for (PrecinctNode n : nodes) {
            for (PrecinctEdge e : n.getAdjacentEdges()) {
                if (e.size() == 0) {
                    UnorderedPair<GerryNode> pair = edgeMap.get(e.getId());
                    e.add((PrecinctNode) pair.getItem1());
                    e.add((PrecinctNode) pair.getItem2());
                }
            }
        }
    }

    /**
     * Fills in all edges' node references after the StateNode has been deserialized from the DB.
     * <p>
     * As edge node references are NOT stored in the DB, these will have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    public void fillInEdgeNodeReferences() {
        final Map<String, UnorderedPair<GerryNode>> edgeMap = new HashMap<>();

        // Load all districts & precincts.
        final Set<DistrictNode> districts = this.children;
        final Set<PrecinctNode> precincts = new HashSet<>();
        districts.forEach(d -> precincts.addAll(d.getChildren()));

        // Find all pairs of adjacent nodes.
        StateNode.fillAdjacentNodes(edgeMap, districts);
        StateNode.fillAdjacentNodes(edgeMap, precincts);

        // Fill all edge references.
        StateNode.fillDistrictEdgeReferences(edgeMap, districts);
        StateNode.fillPrecinctEdgeReferences(edgeMap, precincts);
    }

    @JsonIgnore
    public Set<PrecinctNode> getAllPrecincts() {
        return this.children.stream()
                .flatMap(d -> d.getChildren().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Return a Map whose keys are precincts and values are districts.
     *
     * @return a Map that maps precincts to the desired parents
     * @throws IllegalArgumentException if userDistrict is set to true but the StateNode is an original node
     */
    @JsonIgnore
    public Map<PrecinctNode, DistrictNode> getPrecinctToDistrictMap() {
        Set<PrecinctNode> allPrecincts = this.getAllPrecincts();
        return allPrecincts.stream().collect(Collectors.toMap(Function.identity(), GerryNode::getParent));
    }

    public void executeMove(PrecinctMove move) throws MismatchedElectionException {
        DistrictNode oldDistrict = move.getOldDistrict();
        DistrictNode newDistrict = move.getNewDistrict();
        oldDistrict.removeBorderPrecinct(move.getPrecinct(), true);
        newDistrict.addBorderPrecinct(move.getPrecinct(), true);
    }

    public StateNode copyAndExecuteMove(PrecinctMove move) throws MismatchedElectionException {
        // NOTE: the StateNode produced by this method needs to have its DistrictEdges fixed later.
        Map<DistrictNode, DistrictNode> newDistricts = move.getNewDistricts();
        StateNode newState = StateNode.builder()
                .id(UUID.randomUUID().toString())
                .name(this.name)
                .nodeType(NodeType.USER)
                .demographicData(new DemographicData(this.demographicData))
                .electionData(new ElectionData(this.electionData))
                .districts(new HashSet<>(this.children))
                .build();
        newState.children = newState.children.stream()
                .map(d -> newDistricts.getOrDefault(d, new DistrictNode(d)))
                .collect(Collectors.toSet());
        newState.children.forEach(d -> d.setParent(newState));
        return newState;
    }

    public int getNumMajMinDistricts(Set<DemographicType> demoTypes) {
        int numMajMinDistricts = 0;
        for (DistrictNode d : this.children) {
            if (d.getDemographicData().constitutesMajority(demoTypes)) {
                numMajMinDistricts++;
            }
        }
        return numMajMinDistricts;
    }


}
