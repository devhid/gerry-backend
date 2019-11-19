package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.StateEdge;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties({"adjacentEdges", "adjacentNodes", "parent", "allPrecincts", "precinctToDistrictMap", "electionType"})
public class StateNode extends ClusterNode<StateEdge, DistrictNode> {

    @Getter
    private StateType stateType;

    public StateNode(){
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.stateType = StateType.NOT_SET;
    }

    public StateNode(NodeType nodeType, StateType stateType, Set<DistrictNode> nodes){
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.nodeType = nodeType;
        this.stateType = stateType;
        this.children = nodes;
        this.loadAllCounties();
    }

    public StateNode(StateNode obj, Map<DistrictNode,DistrictNode> changedDistricts){
        this(UUID.randomUUID().toString(), obj.name, NodeType.USER, new DemographicData(obj.demographicData),
                new ElectionData(obj.electionData), null, new HashSet<>(obj.children),
                new HashSet<>(obj.counties), obj.stateType);
        this.children = this.children.stream()
                .map(d -> changedDistricts.getOrDefault(d, new DistrictNode(d)))
                .collect(Collectors.toSet());
        this.children.forEach(d -> d.parent = this);
    }

    // TODO: remove later, for testing insertion of data
    public StateNode(String id, String name, NodeType nodeType, Set<DistrictNode> districts, String geography,
                     Set<String> counties, StateType stateType) throws MismatchedElectionException {
        this(id, name, nodeType, null, null, geography, districts, counties, stateType);
        this.setDistricts(districts);
    }

    public StateNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                     ElectionData electionData, String geography, Set<DistrictNode> districts, Set<String> counties,
                     StateType stateType){
        super(id, name, nodeType, demographicData, electionData, null, geography, districts, counties, null);
        this.stateType = stateType;
    }

    private void setDistricts(Set<DistrictNode> districts) throws MismatchedElectionException {
        this.children = districts;
        ElectionData districtElection = null;
        DemographicData districtDemographics = null;
        for (DistrictNode d : districts){
            if (districtElection == null || districtDemographics == null){
                districtElection = d.getElectionData();
                districtDemographics = d.getDemographicData();
            }
            else {
                districtElection = ElectionData.combine(districtElection, d.getElectionData());
                districtDemographics = DemographicData.combine(districtDemographics, d.getDemographicData());
            }
            d.setState(this);
        }
        this.electionData = districtElection;
        this.demographicData = districtDemographics;
        this.loadAllCounties();
    }

    @Override
    protected void loadAllCounties(){
        this.counties = this.children.stream().flatMap(d -> d.getCounties().stream()).collect(Collectors.toSet());
    }

    public void remapDistrictReferences(Map<DistrictNode,DistrictNode> changedDistricts){
        this.children = this.children.stream()
                .map(d -> changedDistricts.getOrDefault(d, d))
                .collect(Collectors.toSet());
    }

    /**
     * Fills in all properties marked Transient in the graph represented by this StateNode.
     * It calls:
     * - fillInPrecinctUserDistrictReferences
     * - fillInEdgeNodeReferences
     */
    public void fillInTransientProperties(){
        this.fillInParentReferences();
        this.fillInEdgeNodeReferences();
    }

    /**
     * Fills in all nodes' parent references after the StateNode has been deserialized.
     *
     * As parent references are not stored in the DB, these wil have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    private void fillInParentReferences(){
        if (this.nodeType != NodeType.USER){
            return;
        }
        Set<DistrictNode> districts = this.getChildren();
        districts.forEach(d -> {
            d.setParent(this);
            d.getChildren().forEach(p -> {
                p.setParent(d);
            });
        });
    }

    /**
     * Fills in all edges' node references after the StateNode has been deserialized from the DB.
     *
     * As edge node references are NOT stored in the DB, these will have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    private void fillInEdgeNodeReferences(){
        Map<String, UnorderedPair<GerryNode>> edgeMap = new HashMap<>();

        // Load all districts & precincts.
        Set<DistrictNode> districts = this.children;
        Set<PrecinctNode> precincts = new HashSet<>();
        districts.forEach(d -> precincts.addAll(d.getChildren()));

        // Load all edges.
        MapUtils.populateEdgeNodeReferences(edgeMap, districts);
        MapUtils.populateEdgeNodeReferences(edgeMap, precincts);

        districts.forEach(GerryNode::clearEdges);
        precincts.forEach(PrecinctNode::clearEdges);

        // Update all node references in the edges.
        edgeMap.entrySet().forEach(entry -> {
            String edgeId = entry.getKey();
            UnorderedPair<GerryNode> edgeNodes = entry.getValue();
            if (edgeNodes.getItem1() instanceof PrecinctNode){
                PrecinctNode p1 = (PrecinctNode) edgeNodes.getItem1();
                PrecinctNode p2 = (PrecinctNode) edgeNodes.getItem2();
                PrecinctEdge edge = new PrecinctEdge(edgeId, p1, p2);
                try {
                    p1.addEdge(edge);
                    p2.addEdge(edge);
                } catch (InvalidEdgeException e) {
                    e.printStackTrace();
                }
            }
            else if (edgeNodes.getItem2() instanceof DistrictNode){
                DistrictNode d1 = (DistrictNode) edgeNodes.getItem1();
                DistrictNode d2 = (DistrictNode) edgeNodes.getItem2();
                DistrictEdge edge = new DistrictEdge(edgeId, d1, d2);
                try {
                    d1.addEdge(edge);
                    d2.addEdge(edge);
                } catch (InvalidEdgeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Set<PrecinctNode> getAllPrecincts(){
        return this.children.stream()
                .flatMap(d -> d.getChildren().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Return a Map whose keys are precincts and values are districts.
     * @return a Map that maps precincts to the desired parents
     * @throws IllegalArgumentException if userDistrict is set to true but the StateNode is an original node
     */
    public Map<PrecinctNode,DistrictNode> getPrecinctToDistrictMap(){
        Set<PrecinctNode> allPrecincts = this.getAllPrecincts();
        return allPrecincts.stream().collect(Collectors.toMap(Function.identity(), GerryNode::getParent));
    }

    public PhaseOneMergeDelta executeDistrictMerge(CandidatePairs pairs){
        // TODO: fill in
        return null;
    }

    public StateNode copyAndExecuteMove(PrecinctMove move){
        Map<DistrictNode,DistrictNode> newDistricts = move.computeNewDistricts();
        return new StateNode(this, newDistricts);
    }


}
