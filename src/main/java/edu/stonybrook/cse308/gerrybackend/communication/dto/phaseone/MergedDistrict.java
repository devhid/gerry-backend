package edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MergedDistrict {

    @Getter
    private String numericalId;

    @Getter
    private DemographicData demographicData;

    @Getter
    private ElectionData electionData;

    @Getter
    private Set<String> precinctNames;

    @Getter
    private boolean isMajMin;

    public MergedDistrict(String numericalId, DemographicData demographicData, ElectionData electionData,
                          Set<String> precinctNames, boolean isMajMin) {
        this.numericalId = numericalId;
        this.demographicData = demographicData;
        this.electionData = electionData;
        this.precinctNames = precinctNames;
        this.isMajMin = isMajMin;
    }

    public static MergedDistrict fromDistrictNode(DistrictNode district, Set<DemographicType> demoTypes) {
        Set<String> precinctNames;
        if (district.getChildren() != null) {
            precinctNames = district.getChildren().stream().map(GerryNode::getName).collect(Collectors.toSet());
        } else {
            precinctNames = new HashSet<>();
        }
        return new MergedDistrict(district.getNumericalId(), district.getDemographicData(), district.getElectionData(),
                precinctNames, district.getDemographicData().constitutesMajority(demoTypes));
    }

}
