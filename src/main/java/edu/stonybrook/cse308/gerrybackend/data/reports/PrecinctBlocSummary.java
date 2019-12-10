package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class PrecinctBlocSummary {

    @Getter
    private int votingBlocCount;

    @Getter
    private double meanPartyPercentage;

    private double totalPartyPercentage;

    @Getter
    private DemographicType demographicType;

    @Getter
    private double meanDemographicPercentage;

    private double totalDemographicPercentage;

    @Getter
    private Set<String> precinctNames;

    @Getter
    private Set<String> counties;

    public PrecinctBlocSummary(final DemographicType demographicType) {
        this.demographicType = demographicType;
        this.precinctNames = new HashSet<>();
        this.counties = new HashSet<>();
    }

    public void addPartyPercentage(double percentage) {
        this.totalPartyPercentage += percentage;
        this.meanPartyPercentage = this.totalPartyPercentage / this.votingBlocCount;
    }

    public void addDemographicPercentage(double percentage) {
        this.totalDemographicPercentage += percentage;
        this.meanDemographicPercentage = this.totalDemographicPercentage / this.votingBlocCount;
    }

    public void incrementVotingBlocCount() {
        this.votingBlocCount++;
    }

    public void addPrecinct(PrecinctNode precinct) {
        this.precinctNames.add(precinct.getName());
        this.counties.add(precinct.getCounty());
    }

}
