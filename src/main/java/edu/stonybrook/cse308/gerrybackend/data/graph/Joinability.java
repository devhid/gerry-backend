package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Embeddable
public class Joinability {

    @Column(name = "population")
    private double population;

    @Column(name = "compactness")
    private double compactness;

    @ElementCollection
    @Column(name = "political")
    private Map<PoliticalParty, Double> political;

    @ElementCollection
    @Column(name = "minority")
    private Map<DemographicType, Double> minority;

    public Joinability() {
    }

    public Joinability(GerryNode node1, GerryNode node2) {
        this.computeJoinability(node1, node2);
    }

    private void computeJoinability(GerryNode node1, GerryNode node2) {
        this.political = Joinability.computePoliticalJoinability(node1, node2);
        this.population = Joinability.computePopulationJoinability(node1, node2);
        this.minority = Joinability.computeMinorityJoinability(node1, node2);
        this.compactness = Joinability.computeCompactnessJoinability(node1, node2);
    }

    private static double computePopulationJoinability(GerryNode node1, GerryNode node2) {
        return -1.0;
    }

    private static double computeCompactnessJoinability(GerryNode node1, GerryNode node2) {
        return -1.0;
    }

    private static Map<PoliticalParty, Double> computePoliticalJoinability(GerryNode node1, GerryNode node2) {
        Map<PoliticalParty, Double> politicalJoinability = new EnumMap<>(PoliticalParty.class);
        return politicalJoinability;
    }

    private static Map<DemographicType, Double> computeMinorityJoinability(GerryNode node1, GerryNode node2) {
        Map<DemographicType, Double> minorityJoinability = new EnumMap<>(DemographicType.class);
        return minorityJoinability;
    }

    public double getValueWithoutMinority(Set<PoliticalParty> politicalParties) {
        double maxPoliticalJoinability = 0.0;
        for (PoliticalParty party: politicalParties) {
            maxPoliticalJoinability = Math.max(maxPoliticalJoinability, this.political.get(party));
        }

        return this.population + this.compactness + maxPoliticalJoinability;
    }

    public double getValue(Set<PoliticalParty> politicalParties, Set<DemographicType> demoTypes) {
        // fake math
        double minorityAvg = 0.0;

        for (DemographicType demoType : demoTypes) {
            minorityAvg += this.minority.get(demoType);
        }

        double maxPoliticalJoinability = 0.0;
        for (PoliticalParty party: politicalParties) {
            maxPoliticalJoinability = Math.max(maxPoliticalJoinability, this.political.get(party));
        }

        return this.population + this.compactness + maxPoliticalJoinability + minorityAvg;
    }
}
