package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.StateEdge;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"adjacentEdges", "adjacentNodes", "parent"})
public class StateNode extends ClusterNode<StateEdge, DistrictNode> {

    @Getter
    private StateType stateType;

    public StateNode(){
        super();
        this.adjacentEdges = null;
        this.parent = null;
        this.stateType = StateType.NOT_SET;
    }

    public StateNode(DistrictNode defaultDistrict){
        this();
        Set<DistrictNode> districts = new HashSet<>();
        districts.add(defaultDistrict);
        this.setDistricts(districts);
    }

    public StateNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                     ElectionData electionData, String geography, Set<DistrictNode> districts, StateType stateType){
        super(id, name, nodeType, demographicData, electionData, null, geography, districts, new HashSet<>(), null);
        this.stateType = stateType;
        this.loadAllCounties();
    }

    private void loadAllCounties(){
        Set<String> allCounties = new HashSet<>();
        Set<DistrictNode> districts = this.getNodes();
        for (DistrictNode district : districts){
            allCounties.addAll(district.getCounties());
        }
        this.counties = allCounties;
    }

    private void setDistricts(Set<DistrictNode> districts){
        this.nodes = districts;
    }

    /**
     * Fills in all edges' node references after the StateNode has been deserialized from the DB.
     *
     * As edge node references are NOT stored in the DB, these will have to be populated afterwards.
     * Make sure to call this method after deserialization of a StateNode.
     */
    public void fillInEdgeNodeReferences(){
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
}
