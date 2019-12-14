package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.algorithms.measures.CompactnessMeasure;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.ClusterNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.utils.MathUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class Joinability {

    private double compactness;
    private double population;
    private Map<DemographicType, Double> minority;
    private Map<PoliticalParty, Double> political;

    public Joinability() {
    }

    public Joinability(GerryNode node1, GerryNode node2) {
        this.computeJoinability(node1, node2);
    }

    private void computeJoinability(GerryNode node1, GerryNode node2) {
        this.compactness = Joinability.computeCompactnessJoinability(node1, node2);
        this.political = Joinability.computePoliticalJoinability(node1, node2);
        this.minority = Joinability.computeMinorityJoinability(node1, node2);
        this.population = Joinability.computePopulationJoinability(node1, node2);
    }

    private static double computeCompactnessJoinability(GerryNode node1, GerryNode node2) {
        if (node1 instanceof PrecinctNode && node2 instanceof PrecinctNode) {
            PrecinctNode p1 = (PrecinctNode) node1;
            PrecinctNode p2 = (PrecinctNode) node2;
            return (p1.getCounty().equals(p2.getCounty())) ? 1.0 : 0.0;
        } else if (node1 instanceof DistrictNode && node2 instanceof DistrictNode) {
            DistrictNode d1 = (DistrictNode) node1;
            DistrictNode d2 = (DistrictNode) node2;
            try {
                DistrictNode potentialMerge = DistrictNode.combine(d1, d2, false);
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

    public double getValueWithoutMinority(Set<PoliticalParty> politicalParties) {
        double maxPoliticalJoinability = 0.0;
        for (PoliticalParty party : politicalParties) {
            maxPoliticalJoinability = Math.max(maxPoliticalJoinability, this.political.get(party));
        }
        double sum = this.compactness + this.population + maxPoliticalJoinability;
        if (sum < 0) {
            throw new IllegalArgumentException("woah");
        }
        return sum;
//        return this.compactness + this.population + maxPoliticalJoinability;
    }

    public double getValue(Set<PoliticalParty> politicalParties, Set<DemographicType> demoTypes) {
        double minority = 0.0;
        double maxPoliticalJoinability = 0.0;
        for (DemographicType demoType : demoTypes) {
            minority += this.minority.get(demoType);
        }
        for (PoliticalParty party : politicalParties) {
            maxPoliticalJoinability = Math.max(maxPoliticalJoinability, this.political.get(party));
        }
        return this.compactness + this.population + maxPoliticalJoinability + minority;
    }
}
