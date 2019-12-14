package edu.stonybrook.cse308.gerrybackend.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Embeddable
@JsonIgnoreProperties({"populationCopy", "votingAgePopulationCopy"})
public class DemographicData {

    @NotNull
    @BatchSize(size = 500)  // IMPORTANT
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "population")
    @JsonProperty("population")
    private Map<DemographicType, Integer> population;

    @NotNull
    @BatchSize(size = 500)  // IMPORTANT
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "voting_age_population")
    @JsonProperty("voting_age_population")
    private Map<DemographicType, Integer> votingAgePopulation;

    public DemographicData() {
        this.population = new EnumMap<>(DemographicType.class);
        this.votingAgePopulation = new EnumMap<>(DemographicType.class);
        MapUtils.initMap(this.population, 0);
        MapUtils.initMap(this.votingAgePopulation, 0);
    }

    public DemographicData(Map<DemographicType, Integer> population,
                           Map<DemographicType, Integer> votingAgePopulation) {
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
    }

    public DemographicData(DemographicData obj) {
        this(obj.getPopulationCopy(), obj.getVotingAgePopulationCopy());
    }

    public int getDemoPopulation(DemographicType demo) {
        return this.population.get(demo);
    }

    public int getDemoPopulation(Set<DemographicType> demoTypes) {
        int demoPop = 0;
        for (DemographicType demoType : demoTypes) {
            demoPop += this.population.get(demoType);
        }
        return demoPop;
    }

    public boolean constitutesMajority(Set<DemographicType> demoTypes) {
        int demoPopulation = this.getDemoPopulation(demoTypes);
        return (((double) demoPopulation) / this.getTotalPopulation()) > 0.5;
    }

    public int getDemoPopulation(boolean canVote, DemographicType demo) {
        if (!canVote) {
            return this.population.get(demo) - this.votingAgePopulation.get(demo);
        }
        return this.votingAgePopulation.get(demo);
    }

    public int getDemoPopulation(boolean canVote, Set<DemographicType> demoTypes) {
        int demoPop = 0;
        for (DemographicType demoType : demoTypes) {
            demoPop += this.population.get(demoType);
            if (!canVote) {
                demoPop -= this.votingAgePopulation.get(demoType);
            }
        }
        return demoPop;
    }

    public int getTotalPopulation() {
        return MapUtils.sumIntValuedMap(this.population);
    }

    public int getTotalVotingAgePopulation() {
        return MapUtils.sumIntValuedMap(this.votingAgePopulation);
    }

    public void setDemoPopulation(DemographicType demo, int demoPop) {
        this.population.put(demo, demoPop);
    }

    public void setVotingAgeDemoPopulation(DemographicType demo, int demoPop) {
        this.votingAgePopulation.put(demo, demoPop);
    }

    public Map<DemographicType, Integer> getPopulationCopy() {
        return new EnumMap<>(this.population);
    }

    public Map<DemographicType, Integer> getVotingAgePopulationCopy() {
        return new EnumMap<>(this.votingAgePopulation);
    }

    public static DemographicData combine(DemographicData d1, DemographicData d2) {
        Map<DemographicType, Integer> d1Pop = d1.population;
        Map<DemographicType, Integer> d1VotingAgePop = d1.votingAgePopulation;
        Map<DemographicType, Integer> d2Pop = d2.population;
        Map<DemographicType, Integer> d2VotingAgePop = d2.votingAgePopulation;

        Set<DemographicType> demoTypes = d1Pop.keySet();
        Map<DemographicType, Integer> combinedPop = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> combinedVotingAgePop = new EnumMap<>(DemographicType.class);
        for (DemographicType demoType : demoTypes) {
            int sumDemoPop = d1Pop.getOrDefault(demoType, 0) + d2Pop.getOrDefault(demoType, 0);
            int sumVotingAgeDemoPop = d1VotingAgePop.getOrDefault(demoType, 0) + d2VotingAgePop.getOrDefault(demoType, 0);
            combinedPop.put(demoType, sumDemoPop);
            combinedVotingAgePop.put(demoType, sumVotingAgeDemoPop);
        }
        return new DemographicData(combinedPop, combinedVotingAgePop);
    }

    public void subtract(DemographicData subDemoData) {
        Map<DemographicType, Integer> subPop = subDemoData.population;
        Map<DemographicType, Integer> subVotingAgePop = subDemoData.votingAgePopulation;

        for (DemographicType demoType : this.population.keySet()) {
            int bigDemoPop = this.population.getOrDefault(demoType, 0);
            int bigVotingAgeDemoPop = this.votingAgePopulation.getOrDefault(demoType, 0);
            int smallDemoPop = subPop.getOrDefault(demoType, 0);
            int smallVotingAgeDemoPop = subVotingAgePop.getOrDefault(demoType, 0);
            if ((bigDemoPop < smallDemoPop) ||
                    (bigVotingAgeDemoPop < smallVotingAgeDemoPop)) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            this.population.put(demoType, bigDemoPop - smallDemoPop);
            this.votingAgePopulation.put(demoType, bigVotingAgeDemoPop - smallVotingAgeDemoPop);
        }
    }

}
