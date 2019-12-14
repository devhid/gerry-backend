package edu.stonybrook.cse308.gerrybackend.data.comparators;

import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import java.util.Comparator;

public class SmallestLikelyPairsComparator implements Comparator<LikelyCandidatePair> {

    @Override
    public int compare(LikelyCandidatePair o1, LikelyCandidatePair o2) {
        DistrictNode d1 = o1.getItem1();
        DistrictNode d2 = o1.getItem2();
        DistrictNode d3 = o2.getItem1();
        DistrictNode d4 = o2.getItem2();
        int o1Population = d1.getDemographicData().getTotalPopulation() + d2.getDemographicData().getTotalPopulation();
        int o2Population = d3.getDemographicData().getTotalPopulation() + d4.getDemographicData().getTotalPopulation();
        return (o1Population - o2Population);
    }

}
