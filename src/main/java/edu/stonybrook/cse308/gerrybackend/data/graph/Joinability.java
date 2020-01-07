package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.algorithms.measures.CompactnessMeasure;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.utils.MathUtils;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class Joinability {

    @Getter
    private double county;                          // 0 or 1

    @Getter
    private double compactness;                     // [0,1]

    @Getter
    private double population;                      // [0,2]

    private Map<DemographicType, Double> minority;  // [0,1] for each
    private Map<PoliticalParty, Double> political;  // [0,1] for each

    public Joinability() {
    }

    public Joinability(GerryNode node1, GerryNode node2) {
        this.computeJoinability(node1, node2);
    }

    private void computeJoinability(GerryNode node1, GerryNode node2) {
        this.county = Joinability.computeCountyJoinability(node1, node2);
        this.compactness = Joinability.computeCompactnessJoinability(node1, node2);
        this.political = Joinability.computePoliticalJoinability(node1, node2);
        this.minority = Joinability.computeMinorityJoinability(node1, node2);
        this.population = Joinability.computePopulationJoinability(node1, node2);
    }

    private static double computeCountyJoinability(GerryNode node1, GerryNode node2) {
        if (node1 instanceof PrecinctNode && node2 instanceof PrecinctNode) {
            PrecinctNode p1 = (PrecinctNode) node1;
            PrecinctNode p2 = (PrecinctNode) node2;
            return (p1.getCounty().equals(p2.getCounty())) ? 1.0 : 0.0;
        } else if (node1 instanceof DistrictNode && node2 instanceof DistrictNode) {
            DistrictNode d1 = (DistrictNode) node1;
            DistrictNode d2 = (DistrictNode) node2;
            if (d1.getCounties().equals(d2.getCounties())) {
                return 1.0;
            } else {
                return 0.0;
            }
        }
        return 0.0;
    }

    private static double computeCompactnessJoinability(GerryNode node1, GerryNode node2) {
        if (node1 instanceof PrecinctNode && node2 instanceof PrecinctNode) {
            PrecinctNode p1 = (PrecinctNode) node1;
            PrecinctNode p2 = (PrecinctNode) node2;
            DistrictNode potentialMerge = DistrictNode.childBuilder()
                    .child(p1)
                    .nodeType(NodeType.USER)
                    .build();
            try {
                potentialMerge.addBorderPrecinct(p2, false);
                return CompactnessMeasure.computeCompactnessScore(Compactness.CONVEX_HULL, potentialMerge);
            } catch (MismatchedElectionException e) {
                e.printStackTrace();
            }
        } else if (node1 instanceof DistrictNode && node2 instanceof DistrictNode) {
            DistrictNode d1 = (DistrictNode) node1;
            DistrictNode d2 = (DistrictNode) node2;
            try {
                DistrictNode potentialMerge = DistrictNode.combineForStatisticsOnly(d1, d2);
                return CompactnessMeasure.computeCompactnessScore(Compactness.CONVEX_HULL, potentialMerge);
            } catch (IllegalArgumentException e) {
              // do nothing
            } catch (MismatchedElectionException e) {
                e.printStackTrace();
            }
            return 0.0;
        }
        return 0.0;
    }

    private static double computePopulationJoinability(GerryNode node1, GerryNode node2) {
        DemographicData d1 = node1.getDemographicData();
        DemographicData d2 = node2.getDemographicData();
        double population1 = d1.getTotalPopulation();
        double population2 = d2.getTotalPopulation();
        return MathUtils.calculatePercentDifference(population1, population2);
    }

    private static Map<DemographicType, Double> computeMinorityJoinability(GerryNode node1, GerryNode node2) {
        Map<DemographicType, Double> minorityJoinability = new EnumMap<>(DemographicType.class);
        for (DemographicType demoType : DemographicType.values()) {
            if (demoType == DemographicType.getDefault()) {
                continue;
            }
            DemographicData d1 = node1.getDemographicData();
            DemographicData d2 = node2.getDemographicData();
            double demoProportion1 = ((double) d1.getDemoPopulation(demoType)) / d1.getTotalPopulation();
            double demoProportion2 = ((double) d2.getDemoPopulation(demoType)) / d2.getTotalPopulation();
            minorityJoinability.put(demoType, 1.0 - MathUtils.calculatePercentDifference(demoProportion1, demoProportion2));
        }
        return minorityJoinability;
    }

    private static Map<PoliticalParty, Double> computePoliticalJoinability(GerryNode node1, GerryNode node2) {
        Map<PoliticalParty, Double> politicalJoinability = new EnumMap<>(PoliticalParty.class);
        for (PoliticalParty party : PoliticalParty.values()) {
            if (party == PoliticalParty.getDefault()) {
                continue;
            }
            ElectionData e1 = node1.getElectionData();
            ElectionData e2 = node2.getElectionData();
            double partyProportion1 = ((double) e1.getPartyVotes(party))/ e1.getTotalVotes();
            double partyProportion2 = ((double) e2.getPartyVotes(party))/ e2.getTotalVotes();
            politicalJoinability.put(party, 1.0 - MathUtils.calculatePercentDifference(partyProportion1, partyProportion2));
        }
        return politicalJoinability;
    }

    public double getMinority(Set<DemographicType> demoTypes) {
        double minorityAvg = 0.0;
        for (DemographicType demoType : demoTypes) {
            minorityAvg += this.minority.get(demoType);
        }
        return minorityAvg / demoTypes.size();
    }

    public double getPolitical(Set<PoliticalParty> politicalParties) {
        double maxPoliticalJoinability = 0.0;
        for (PoliticalParty party : politicalParties) {
            maxPoliticalJoinability = Math.max(maxPoliticalJoinability, this.political.get(party));
        }
        return maxPoliticalJoinability;
    }

    public double getValueWithoutMinority(Set<PoliticalParty> politicalParties) {
        return this.county + this.compactness + this.population + this.getPolitical(politicalParties);
    }

    public double getValue(Set<PoliticalParty> politicalParties, Set<DemographicType> demoTypes) {
        return this.getValueWithoutMinority(politicalParties) + this.getMinority(demoTypes);
    }
}
