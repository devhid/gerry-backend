package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class PrecinctBlocSummary {

    @Getter
    @Setter
    private int votingBlocCount;

    @Getter
    @Setter
    private PoliticalParty partyType;

    @Getter
    @Setter
    private double meanPartyPercentage;

    private double totalPartyPercentage;

    @Getter
    @Setter
    private DemographicType demographicType;

    @Getter
    @Setter
    private double meanDemographicPercentage;

    private double totalDemographicPercentage;

    private Set<String> counties;

    public PrecinctBlocSummary(final PoliticalParty partyType, final DemographicType demographicType) {
        this.partyType = partyType;
        this.demographicType = demographicType;
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

    public void addCounty(String county){
        this.counties.add(county);
    }
}
