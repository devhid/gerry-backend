package edu.stonybrook.cse308.gerrybackend.communication.dto;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.ClusterNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class ClusterNodeStatistics extends GerryNodeStatistics {

    @Getter
    private Set<GerryNodeStatistics> children;

    public ClusterNodeStatistics(String id, String name, DemographicData demographicData, ElectionData electionData,
                                 Set<GerryNodeStatistics> children) {
        super(id, name, demographicData, electionData);
        this.children = children;
    }

    public static ClusterNodeStatistics fromClusterNode(ClusterNode<GerryEdge, GerryNode> clusterNode) {
        Set<GerryNodeStatistics> children = new HashSet<>();
        for (GerryNode child : clusterNode.getChildren()) {
            children.add(GerryNodeStatistics.fromGerryNode(child));
        }
        return new ClusterNodeStatistics(clusterNode.getId(), clusterNode.getName(), clusterNode.getDemographicData(),
                clusterNode.getElectionData(), children);
    }
}
