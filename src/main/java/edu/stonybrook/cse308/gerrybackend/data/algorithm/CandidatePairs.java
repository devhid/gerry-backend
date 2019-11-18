package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CandidatePairs {

    private SortedSet<LikelyCandidatePair> majorityMinorityPairs;
    private SortedSet<LikelyCandidatePair> otherPairs;

    public CandidatePairs(Set<LikelyCandidatePair> majorityMinorityPairs,
                          Set<LikelyCandidatePair> otherPairs){
        this.majorityMinorityPairs = new TreeSet<>(majorityMinorityPairs);
        this.otherPairs = new TreeSet<>(otherPairs);
    }

    public CandidatePairs(SortedSet<LikelyCandidatePair> majorityMinorityPairs,
                          SortedSet<LikelyCandidatePair> otherPairs){
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
