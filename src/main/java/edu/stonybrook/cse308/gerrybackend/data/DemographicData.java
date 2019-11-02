package edu.stonybrook.cse308.gerrybackend.data;

import edu.stonybrook.cse308.gerrybackend.enums.DemographicType;

import java.util.stream.Stream;

public class DemographicData {

    private final int[] population;
    private final int[] votingAgePopulation;

    public DemographicData(int[] population, int[] votingAgePopulation){
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
    }

    public int getPopulation(DemographicType demo){
        return this.population[demo.value];
    }

    public int getPopulation(boolean canVote, DemographicType demo){
        if (!canVote){
            return this.getPopulation(demo) - this.votingAgePopulation[demo.value];
        }
        return this.votingAgePopulation[demo.value];
    }

    public int[] getPopulationCopy() {
        int[] pop = new int[this.population.length];
        System.arraycopy(this.population, 0, pop, 0, this.population.length);
        return pop;
    }

    public int[] getVotingAgePopulationCopy(){
        int[] votingAgePop = new int[this.votingAgePopulation.length];
        System.arraycopy(this.votingAgePopulation, 0, votingAgePop, 0, this.votingAgePopulation.length);
        return votingAgePop;
    }

    public static DemographicData combine(DemographicData d1, DemographicData d2){
        int[] d1Pop = d1.getPopulationCopy();
        int[] d1VotingAgePop = d1.getVotingAgePopulationCopy();
        int[] d2Pop = d2.getPopulationCopy();
        int[] d2VotingAgePop = d2.getVotingAgePopulationCopy();

        DemographicType[] demoTypes = DemographicType.values();
        int[] combinedPop = new int[demoTypes.length];
        int[] combinedVotingAgePop = new int[demoTypes.length];
        Stream.of(demoTypes).forEach(val -> {
            int index = val.value;
            combinedPop[index] = d1Pop[index] + d2Pop[index];
            combinedVotingAgePop[index] = d1VotingAgePop[index] + d2VotingAgePop[index];
        });
        return new DemographicData(combinedPop, combinedVotingAgePop);
    }

}
