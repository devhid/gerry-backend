package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;
import lombok.Setter;

public class LikelyCandidatePair extends UnorderedPair<DistrictNode> implements Comparable<LikelyCandidatePair> {

    @Getter
    private LikelyType likelyType;

    @Getter
    @Setter
    private double joinabilityScore;

    public LikelyCandidatePair(DistrictNode d1, DistrictNode d2, LikelyType likelyType) {
        super(d1, d2);
        this.likelyType = likelyType;
    }

    public LikelyCandidatePair(DistrictNode d1, DistrictNode d2, LikelyType likelyType, double joinabilityScore) {
        super(d1, d2);
        this.likelyType = likelyType;
        this.joinabilityScore = joinabilityScore;
    }

    @Override
    public int compareTo(LikelyCandidatePair o) {
        return (this.likelyType.getValue() - o.likelyType.getValue());
    }
}
