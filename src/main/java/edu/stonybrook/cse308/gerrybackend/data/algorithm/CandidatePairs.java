package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import java.util.Set;

public class CandidatePairs {

    private Set<UnorderedPair<DistrictNode>> majorityMinorityPairs;
    private Set<UnorderedPair<DistrictNode>> otherPairs;

    public CandidatePairs(Set<UnorderedPair<DistrictNode>> majorityMinorityPairs,
                          Set<UnorderedPair<DistrictNode>> otherPairs){
        this.majorityMinorityPairs = majorityMinorityPairs;
        this.otherPairs = otherPairs;
    }

    public int size(){
        return this.majorityMinorityPairs.size() + this.otherPairs.size();
    }

    public void filterPairs(Set<UnorderedPair<DistrictNode>> filteredPairs){
        // TODO: fill in
    }

}
