package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import java.util.Set;

public class CandidatePairs {

    private Set<LikelyCandidatePair> majorityMinorityPairs;
    private Set<LikelyCandidatePair> otherPairs;

    public CandidatePairs(Set<LikelyCandidatePair> majorityMinorityPairs,
                          Set<LikelyCandidatePair> otherPairs){
        this.majorityMinorityPairs = majorityMinorityPairs;
        this.otherPairs = otherPairs;
    }

    public int size(){
        return this.majorityMinorityPairs.size() + this.otherPairs.size();
    }

    public void filterPairs(Set<LikelyCandidatePair> filteredPairs){
        this.majorityMinorityPairs.retainAll(filteredPairs);
        this.otherPairs.retainAll(filteredPairs);
    }

}
