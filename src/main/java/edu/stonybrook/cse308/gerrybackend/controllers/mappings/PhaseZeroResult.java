package edu.stonybrook.cse308.gerrybackend.controllers.mappings;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

import java.util.Map;

public class PhaseZeroResult {

    @Getter
    private String precinctId;
    @Getter
    private DemographicType demographicType;
    @Getter
    private int demographicPopulation;
    @Getter
    private int totalPopulation;
    @Getter
    private PoliticalParty party;
    @Getter
    private int partyVoteCount;
    @Getter
    private int totalVote;

    public PhaseZeroResult(final String precinctId, final Map.Entry<DemographicType, Integer> demographicEntry,
                           final int totalPopulation, final PoliticalParty politicalParty, final int partyVote,
                           final int totalVote) {
        this.precinctId = precinctId;
        this.demographicType = demographicEntry.getKey();
        this.demographicPopulation = demographicEntry.getValue();
        this.totalPopulation = totalPopulation;
        this.party = politicalParty;
        this.partyVoteCount = partyVote;
        this.totalVote = totalVote;
    }

}
