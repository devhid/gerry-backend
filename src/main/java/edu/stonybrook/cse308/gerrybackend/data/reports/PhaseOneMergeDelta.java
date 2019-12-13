package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;

import java.util.Map;

public class PhaseOneMergeDelta extends IterativeAlgPhaseDelta {

    @Getter
    private Map<String, String> changedNodes;

    @Getter
    private Map<String, DistrictNode> newDistricts;

    public PhaseOneMergeDelta(int iteration, Map<String, String> changedNodes, Map<String, DistrictNode> newDistricts) {
        super(iteration);
        this.changedNodes = changedNodes;
        this.newDistricts = newDistricts;
    }

}
