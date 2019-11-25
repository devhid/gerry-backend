package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CandidatePairs {

    @Getter
    private Set<LikelyCandidatePair> majorityMinorityPairs;

    @Getter
    private Set<LikelyCandidatePair> otherPairs;

    public CandidatePairs(Set<LikelyCandidatePair> majorityMinorityPairs,
                          Set<LikelyCandidatePair> otherPairs) {
        this.majorityMinorityPairs = majorityMinorityPairs;
        this.otherPairs = otherPairs;
    }

    public int size() {
        return this.majorityMinorityPairs.size() + this.otherPairs.size();
    }

    public Set<LikelyCandidatePair> getAllPairs() {
        Set<LikelyCandidatePair> allPairs = new HashSet<>(this.majorityMinorityPairs);
        allPairs.addAll(this.otherPairs);
        return allPairs;
    }

    public void filterPairs(Set<LikelyCandidatePair> filteredPairs) {
        this.majorityMinorityPairs.retainAll(filteredPairs);
        this.otherPairs.retainAll(filteredPairs);
    }

}
