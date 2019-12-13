package edu.stonybrook.cse308.gerrybackend.communication.dto.statistics;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.graph.Incumbent;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Measures;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.ClusterNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DistrictNodeStatistics extends ClusterNodeStatistics {

    @Getter
    private Incumbent incumbent;

    public DistrictNodeStatistics(String name, DemographicData demographicData, ElectionData electionData,
                                  Set<GerryNodeStatistics> children, Map<Measures, Double> measures, Incumbent incumbent) {
        super(name, demographicData, electionData, children, measures);
        this.incumbent = incumbent;
    }

    public static DistrictNodeStatistics fromDistrictNode(DistrictNode district) {
        return new DistrictNodeStatistics(district.getName(), district.getDemographicData(), district.getElectionData(),
                null, district.getMeasures(), district.getIncumbent());
    }

}
