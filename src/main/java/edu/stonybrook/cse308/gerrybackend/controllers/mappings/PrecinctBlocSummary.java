package edu.stonybrook.cse308.gerrybackend.controllers.mappings;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;
import lombok.Setter;

public class PrecinctBlocSummary {

    @Getter @Setter
    private int precinctCount;
    @Getter @Setter
    private PoliticalParty partyType;
    @Getter @Setter
    private double meanPartyPercentage;
    private double totalPartyPercentage;
    @Getter @Setter
    private DemographicType demographicType;
    @Getter @Setter
    private double meanDemographicPercentage;
    private double totalDemographicPercentage;

    public PrecinctBlocSummary(final PoliticalParty partyType, final DemographicType demographicType) {
        this.partyType = partyType;
        this.demographicType = demographicType;
    }

    public void addPartyPercentage(double percentage) {
        this.totalPartyPercentage += percentage;
        this.meanPartyPercentage = this.totalPartyPercentage / this.precinctCount;
    }

    public void addDemographicPercentage(double percentage) {
        this.totalDemographicPercentage += percentage;
        this.meanDemographicPercentage = this.totalDemographicPercentage / this.precinctCount;
    }
}
