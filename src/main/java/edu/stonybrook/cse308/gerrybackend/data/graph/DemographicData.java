package edu.stonybrook.cse308.gerrybackend.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity(name="demographic")
@JsonIgnoreProperties({"populationCopy", "votingAgePopulationCopy"})
public class DemographicData {

    @Getter
    @Id
    @Column(name="id")
    private String id;

    @NotNull
    @ElementCollection
    @Column(name="population")
    @JsonProperty("population")
    private Map<DemographicType, Integer> population;

    @NotNull
    @ElementCollection
    @Column(name="voting_age_population")
    @JsonProperty("voting_age_population")
    private Map<DemographicType, Integer> votingAgePopulation;

    public DemographicData(){
        this.id = UUID.randomUUID().toString();
        this.population = new EnumMap<>(DemographicType.class);
        this.votingAgePopulation = new EnumMap<>(DemographicType.class);
        MapUtils.initMap(this.population, 0);
        MapUtils.initMap(this.votingAgePopulation, 0);
    }

    public DemographicData(String id, Map<DemographicType, Integer> population,
                           Map<DemographicType, Integer> votingAgePopulation){
        this.id = id.toString();
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
    }

    public int getDemoPopulation(DemographicType demo){
        return this.population.get(demo);
    }

    public int getDemoPopulation(boolean canVote, DemographicType demo){
        if (!canVote){
            return this.population.get(demo) - this.votingAgePopulation.get(demo);
        }
        return this.votingAgePopulation.get(demo);
    }

    public void setDemoPopulation(DemographicType demo, int demoPop){
        this.population.put(demo, demoPop);
    }

    public void setVotingAgeDemoPopulation(DemographicType demo, int demoPop){
        this.votingAgePopulation.put(demo, demoPop);
    }

    public Map<DemographicType, Integer> getPopulationCopy() {
        return new EnumMap<>(this.population);
    }

    public Map<DemographicType, Integer> getVotingAgePopulationCopy() {
        return new EnumMap<>(this.votingAgePopulation);
    }

    public static DemographicData combine(DemographicData d1, DemographicData d2){
        Map<DemographicType, Integer> d1Pop = d1.population;
        Map<DemographicType, Integer> d1VotingAgePop = d1.votingAgePopulation;
        Map<DemographicType, Integer> d2Pop = d2.population;
        Map<DemographicType, Integer> d2VotingAgePop = d2.votingAgePopulation;

        Set<DemographicType> demoTypes = d1Pop.keySet();
        Map<DemographicType, Integer> combinedPop = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> combinedVotingAgePop = new EnumMap<>(DemographicType.class);
        for (DemographicType demoType : demoTypes){
           combinedPop.put(demoType, d1Pop.get(demoType) + d2Pop.get(demoType));
           combinedVotingAgePop.put(demoType, d1VotingAgePop.get(demoType) + d2VotingAgePop.get(demoType));
        }
        return new DemographicData(UUID.randomUUID().toString(), combinedPop, combinedVotingAgePop);
    }

}
