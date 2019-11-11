package edu.stonybrook.cse308.gerrybackend.data;

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

    @Column(name="population")
    private double population;

    @Column(name="compactness")
    private double compactness;

    @ElementCollection
    @Column(name="political")
    private Map<PoliticalParty, Double> political;

    @ElementCollection
    @Column(name="minority")
    private Map<DemographicType, Double> minority;

    public Joinability(){
        this.political = new EnumMap<>(PoliticalParty.class);
        this.minority = new EnumMap<>(DemographicType.class);
    }

    public Joinability(GerryNode node1, GerryNode node2){
        this.computeJoinability(node1, node2);
    }

    public Joinability(Map<PoliticalParty, Double> political, double population, Map<DemographicType, Double> minority, double compactness){
        this.political = political;
        this.population = population;
        this.minority = minority;
        this.compactness = compactness;
    }

    private void computeJoinability(GerryNode node1, GerryNode node2){
        this.political = this.computePoliticalJoinability(node1, node2);
        this.population = this.computePopulationJoinability(node1, node2);
        this.minority = this.computeMinorityJoinability(node1, node2);
        this.compactness = this.computeCompactnessJoinability(node1, node2);
    }

    private Map<PoliticalParty, Double> computePoliticalJoinability(GerryNode node1, GerryNode node2){
        Map<PoliticalParty, Double> politicalJoinability = new EnumMap<>(PoliticalParty.class);
        return politicalJoinability;
    }

    private double computePopulationJoinability(GerryNode node1, GerryNode node2){
        return -1.0;
    }

    private Map<DemographicType, Double> computeMinorityJoinability(GerryNode node1, GerryNode node2){
        Map<DemographicType, Double> minorityJoinability = new EnumMap<>(DemographicType.class);
        return minorityJoinability;
    }

    private double computeCompactnessJoinability(GerryNode node1, GerryNode node2){
        return -1.0;
    }

    public double getValue(Set<PoliticalParty> partyTypes, Set<DemographicType> demoTypes){
        // fake math
        double politicalAvg = 0.0;
        double minorityAvg = 0.0;

        for (PoliticalParty partyType : partyTypes){
            politicalAvg += this.political.get(partyType);
        }
        for (DemographicType demoType : demoTypes){
            minorityAvg += this.minority.get(demoType);
        }

        return politicalAvg + this.population + minorityAvg + this.compactness;
    }
}
