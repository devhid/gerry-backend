package edu.stonybrook.cse308.gerrybackend.communication.dto.statistics;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Measures;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

public abstract class ClusterNodeStatistics extends GerryNodeStatistics {

    @Getter
    protected Set<GerryNodeStatistics> children;

    @Getter
    private Map<Measures, Double> measures;

    public ClusterNodeStatistics(String name, DemographicData demographicData, ElectionData electionData,
                                 Set<GerryNodeStatistics> children, Map<Measures, Double> measures) {
        super(name, demographicData, electionData);
        this.children = children;
        this.measures = measures;
    }

}
