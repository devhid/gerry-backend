package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;

public class VoteBloc {

    private PoliticalParty winningParty;
    private int winningVotes;
    private int totalVotes;

    public VoteBloc(PoliticalParty winningParty, int winningVotes, int totalVotes) {
        this.winningParty = winningParty;
        this.winningVotes = winningVotes;
        this.totalVotes = totalVotes;
    }

}
