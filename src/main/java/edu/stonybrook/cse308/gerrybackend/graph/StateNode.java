package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@AttributeOverride(name="nodes", column=@Column(name="clusters"))
public class StateNode extends ClusterNode<DistrictNode, StateNode> {

    public StateNode(){
        super();
        this.parent = null;
    }

    public StateNode(UUID id, String name, ElectionType electionId, DemographicData demographicData,
                       ElectionData electionData, Set<GerryEdge> adjacentEdges, String geography,
                       Set<DistrictNode> districts){
        super(id, name, electionId, demographicData, electionData, adjacentEdges, geography, districts, new HashSet<>(), null);
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
