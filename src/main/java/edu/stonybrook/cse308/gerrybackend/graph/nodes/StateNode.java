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
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.StateEdge;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties({"adjacentEdges", "adjacentNodes", "parent", "allPrecincts", "precinctToDistrictMap"})
public class StateNode extends ClusterNode<StateEdge, DistrictNode> {

    @Getter
    private StateType stateType;

    public StateNode(){
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.stateType = StateType.NOT_SET;
    }

    // TODO: remove this constructor, should not be needed after initial testing
    public StateNode(DistrictNode child){
        this();
        Set<DistrictNode> districts = new HashSet<>();
        districts.add(child);
        this.setDistricts(districts);
    }

    public StateNode(NodeType nodeType, StateType stateType, Set<DistrictNode> nodes){
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.nodeType = nodeType;
        this.stateType = stateType;
        this.nodes = nodes;
        this.loadAllCounties();
    }

    public StateNode(StateNode obj, Map<DistrictNode,DistrictNode> changedDistricts){
        this(UUID.randomUUID().toString(), obj.name, NodeType.USER, new DemographicData(obj.demographicData),
                new ElectionData(obj.electionData), null, new HashSet<>(obj.nodes),
                new HashSet<>(obj.counties), obj.stateType);
        this.nodes = this.nodes.stream()
                .map(d -> changedDistricts.getOrDefault(d, new DistrictNode(d)))
                .collect(Collectors.toSet());
        this.nodes.forEach(d -> d.parent = this);
    }

    public StateNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                     ElectionData electionData, String geography, Set<DistrictNode> districts, Set<String> counties,
                     StateType stateType){
        super(id, name, nodeType, demographicData, electionData, null, geography, districts, counties, null);
        this.stateType = stateType;
    }

    private void setDistricts(Set<DistrictNode> districts){
        this.nodes = districts;
    }

    private void loadAllCounties(){
        this.nodes.forEach(d -> this.counties.addAll(d.getCounties()));
    }

    /**
     * Fills in all properties marked Transient in the graph represented by this StateNode.
     * It calls:
     * - fillInPrecinctUserDistrictReferences
     * - fillInEdgeNodeReferences
     */
    public void fillInTransientProperties(){
        this.fillInPrecinctUserDistrictReferences();
        this.fillInEdgeNodeReferences();
    }

    /**
     * Fills in all precincts' userDistrict references after the StateNode has been deserialized.
     *
     * As userDistrict references are not stored in the DB, these wil have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    private void fillInPrecinctUserDistrictReferences(){
        if (this.nodeType != NodeType.USER){
            return;
        }
        Set<DistrictNode> districts = this.getNodes();
        districts.forEach(d -> {
            d.getNodes().forEach(p -> {
                p.setUserDistrict(d);
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
        Map<GerryEdge, UnorderedPair<GerryNode>> edgeMap = new HashMap<>();

        // Load all districts & precincts.
        Set<DistrictNode> districts = this.nodes;
        Set<PrecinctNode> precincts = new HashSet<>();
        districts.forEach(d -> precincts.addAll(d.getNodes()));

        // Load all edges.
        MapUtils.populateEdgeNodeReferences(edgeMap, districts);
        MapUtils.populateEdgeNodeReferences(edgeMap, precincts);

        // Update all node references in the edges.
        edgeMap.entrySet().forEach(entry -> {
            GerryEdge edge = entry.getKey();
            UnorderedPair<GerryNode> edgeNodes = entry.getValue();

            if (edge instanceof PrecinctEdge){
                PrecinctEdge precinctEdge = (PrecinctEdge) edge;
                precinctEdge.add((PrecinctNode) edgeNodes.getItem1());
                precinctEdge.add((PrecinctNode) edgeNodes.getItem2());
            }
            else if (edge instanceof DistrictEdge){
                DistrictEdge districtEdge = (DistrictEdge) edge;
                districtEdge.add((DistrictNode) edgeNodes.getItem1());
                districtEdge.add((DistrictNode) edgeNodes.getItem2());
            }
        });
    }

    public Set<PrecinctNode> getAllPrecincts(){
        return this.nodes.stream()
                .flatMap(d -> d.getNodes().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Return a Map whose keys are precincts and values are districts.
     * Depending on the boolean flag, the values can be either the original districts or the user districts.
     * @param userDistrict determine if the caller wants the user districts or original district
     * @return a Map that maps precincts to the desired parents
     * @throws IllegalArgumentException if userDistrict is set to true but the StateNode is an original node
     */
    public Map<PrecinctNode,DistrictNode> getPrecinctToDistrictMap(boolean userDistrict){
        Set<PrecinctNode> allPrecincts = this.getAllPrecincts();
        if (userDistrict){
            if (this.nodeType == NodeType.ORIGINAL){
                throw new IllegalArgumentException("Replace this string later!");
            }
            return allPrecincts.stream().collect(Collectors.toMap(Function.identity(), PrecinctNode::getUserDistrict));
        }
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
