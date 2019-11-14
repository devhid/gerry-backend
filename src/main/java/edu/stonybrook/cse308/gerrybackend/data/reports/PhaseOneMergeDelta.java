package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import java.util.Map;
import java.util.Set;

public class PhaseOneMergeDelta {

    private int iteration;
    private Map<UnorderedPair<String>, DistrictNode> mergedDistricts;
    private Set<DistrictNode> newDistricts;

}
