package edu.stonybrook.cse308.gerrybackend.data.algorithm;

import edu.stonybrook.cse308.gerrybackend.data.structures.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;

public class LikelyCandidatePair extends UnorderedPair<DistrictNode> implements Comparable<LikelyCandidatePair> {

    @Getter
    private LikelyType likelyType;

    public LikelyCandidatePair(DistrictNode d1, DistrictNode d2, LikelyType likelyType){
        super(d1,d2);
        this.likelyType = likelyType;
    }

    @Override
    public int compareTo(LikelyCandidatePair o) {
        return (this.likelyType.getValue() - o.likelyType.getValue());
    }
}
