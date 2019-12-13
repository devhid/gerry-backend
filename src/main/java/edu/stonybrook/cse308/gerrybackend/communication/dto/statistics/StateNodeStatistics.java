package edu.stonybrook.cse308.gerrybackend.communication.dto.statistics;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Measures;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateNodeStatistics extends ClusterNodeStatistics {

    @Getter
    private String redistrictingLegislation;

    public StateNodeStatistics(String name, DemographicData demographicData, ElectionData electionData,
                               Set<GerryNodeStatistics> children, Map<Measures, Double> measures,
                               String redistrictingLegislation) {
        super(name, demographicData, electionData, children, measures);
        this.redistrictingLegislation = redistrictingLegislation;
    }

    public static StateNodeStatistics fromStateNode(StateNode state) {
        Set<GerryNodeStatistics> children = new HashSet<>();
        for (DistrictNode child : state.getChildren()) {
            children.add(DistrictNodeStatistics.fromDistrictNode(child));
        }
        return new StateNodeStatistics(state.getName(), state.getDemographicData(), state.getElectionData(), children,
                state.getMeasures(), state.getRedistrictingLegislation());
    }
}
