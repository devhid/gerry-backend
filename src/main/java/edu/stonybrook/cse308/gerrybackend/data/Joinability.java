package edu.stonybrook.cse308.gerrybackend.data;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;

import java.util.Set;

public class Joinability {

    private double[] political;
    private double population;
    private double[] minority;
    private double compactness;

    public Joinability(GerryNode node1, GerryNode node2){
        computeJoinabilityValues(node1, node2);
    }

    public Joinability(double[] political, double population, double[] minority, double compactness){
        this.political = political;
        this.population = population;
        this.minority = minority;
        this.compactness = compactness;
    }

    public void computeJoinabilityValues(GerryNode node1, GerryNode node2){
        // TODO: fill in later.
        this.political = new double[DemographicType.values().length];
        this.population = -1.0;
        this.minority = new double[DemographicType.values().length];
        this.compactness = -1.0;
    }

    public double getValue(Set<DemographicType> demoTypes){
        double politicalAvg = 0.0;
        double minorityAvg = 0.0;
        for (DemographicType demoType : demoTypes){
            politicalAvg += this.political[demoType.value];
            minorityAvg += this.minority[demoType.value];
        }
        return politicalAvg + this.population + minorityAvg + this.compactness;
    }
}
