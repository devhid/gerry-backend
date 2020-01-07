package edu.stonybrook.cse308.gerrybackend.communication.dto.statistics;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

public class GerryNodeStatistics {

    @Getter
    protected String name;

    @Getter
    protected DemographicData demographicData;

    @Getter
    protected ElectionData electionData;

    public GerryNodeStatistics(String name, DemographicData demographicData, ElectionData electionData) {
        this.name = name;
        this.demographicData = demographicData;
        this.electionData = electionData;
    }

    public static GerryNodeStatistics fromGerryNode(GerryNode node) {
        return new GerryNodeStatistics(node.getName(), node.getDemographicData(), node.getElectionData());
    }
}
