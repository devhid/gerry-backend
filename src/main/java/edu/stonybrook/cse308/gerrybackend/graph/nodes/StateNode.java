package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class StateNode extends ClusterNode<DistrictEdge, DistrictNode> {

    public StateNode(){
        super();
        this.adjacentEdges = null;
        this.parent = null;
    }

    public StateNode(UUID id, String name, NodeType type, DemographicData demographicData,
                     ElectionData electionData, String geography, Set<DistrictNode> districts){
        super(id, name, type, demographicData, electionData, null, geography, districts, new HashSet<>(), null);
        this.loadAllCounties();
    }

    private void loadAllCounties(){
        Set<String> allCounties = new HashSet<>();
        Set<DistrictNode> districts = this.getDistricts();
        for (DistrictNode district : districts){
            allCounties.addAll(district.getCounties());
        }
        this.counties = allCounties;
    }

    private void setDistricts(Set<DistrictNode> districts){
        this.nodes = districts;
    }

    public Set<DistrictNode> getDistricts(){
        return this.getNodes();
    }
}
