package edu.stonybrook.cse308.gerrybackend.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
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
    @Fetch(FetchMode.SUBSELECT) // IMPORTANT
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "population")
    @JsonProperty("population")
    private Map<DemographicType, Integer> population;

    @NotNull
    @Fetch(FetchMode.SUBSELECT) // IMPORTANT
    @ElementCollection(fetch = FetchType.LAZY)
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

        if (!(d1Pop.keySet().equals(d2Pop.keySet())) || !(d1VotingAgePop.keySet().equals(d2VotingAgePop.keySet()))) {
            throw new IllegalArgumentException("Replace this string later!");
        }

        Set<DemographicType> demoTypes = d1Pop.keySet();
        Map<DemographicType, Integer> combinedPop = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> combinedVotingAgePop = new EnumMap<>(DemographicType.class);
        for (DemographicType demoType : demoTypes) {
            combinedPop.put(demoType, d1Pop.get(demoType) + d2Pop.get(demoType));
            combinedVotingAgePop.put(demoType, d1VotingAgePop.get(demoType) + d2VotingAgePop.get(demoType));
        }
        return new DemographicData(combinedPop, combinedVotingAgePop);
    }

    public void subtract(DemographicData subDemoData) {
        Map<DemographicType, Integer> subPop = subDemoData.population;
        Map<DemographicType, Integer> subVotingAgePop = subDemoData.votingAgePopulation;

        if (!(this.population.keySet().equals(subPop.keySet())) ||
                !(this.votingAgePopulation.keySet().equals(subVotingAgePop.keySet()))) {
            throw new IllegalArgumentException("Replace this string later!");
        }
        for (DemographicType demoType : this.population.keySet()) {
            if ((this.population.get(demoType) < subPop.get(demoType)) ||
                    (this.votingAgePopulation.get(demoType) < subVotingAgePop.get(demoType))) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            this.population.put(demoType, this.population.get(demoType) - subPop.get(demoType));
            this.votingAgePopulation.put(demoType, this.votingAgePopulation.get(demoType) - subVotingAgePop.get(demoType));
        }
    }

}
