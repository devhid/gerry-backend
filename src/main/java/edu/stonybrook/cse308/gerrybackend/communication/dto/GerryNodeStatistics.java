package edu.stonybrook.cse308.gerrybackend.communication.dto;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

public class GerryNodeStatistics {

    @Getter
    protected String id;

    @Getter
    protected String name;

    @Getter
    protected DemographicData demographicData;

    @Getter
    protected ElectionData electionData;

    public GerryNodeStatistics(String id, String name, DemographicData demographicData, ElectionData electionData) {
        this.id = id;
        this.name = name;
        this.demographicData = demographicData;
        this.electionData = electionData;
    }

    public static GerryNodeStatistics fromGerryNode(GerryNode node) {
        return new GerryNodeStatistics(node.getId(), node.getName(), node.getDemographicData(), node.getElectionData());
    }
}
