package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

public class VoteBloc {

    @Getter
    private PoliticalParty winningParty;
    @Getter
    private int winningVotes;
    @Getter
    private int totalVotes;

    public VoteBloc(PoliticalParty winningParty, int winningVotes, int totalVotes) {
        this.winningParty = winningParty;
        this.winningVotes = winningVotes;
        this.totalVotes = totalVotes;
    }

}
