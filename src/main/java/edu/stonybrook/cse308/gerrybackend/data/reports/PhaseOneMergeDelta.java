package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.data.structures.UnorderedStringPair;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;

import java.util.Map;

public class PhaseOneMergeDelta {

    @Getter
    private int iteration;

    @Getter
    private Map<UnorderedStringPair, String> mergedDistricts;

    @Getter
    private Map<String, DistrictNode> newDistricts;

    public PhaseOneMergeDelta(int iteration, Map<UnorderedStringPair, String> mergedDistricts,
                              Map<String, DistrictNode> newDistricts) {
        this.iteration = iteration;
        this.mergedDistricts = mergedDistricts;
        this.newDistricts = newDistricts;
    }
}
