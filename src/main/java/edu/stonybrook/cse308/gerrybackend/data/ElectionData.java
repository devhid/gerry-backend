package edu.stonybrook.cse308.gerrybackend.data;

import edu.stonybrook.cse308.gerrybackend.enums.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ElectionData {

    @Getter
    private final ElectionType electionId;
    private final int[] votes;
    @Getter
    private final PoliticalParty winner;

    public ElectionData(ElectionType electionId, int[] votes, PoliticalParty winner){
        this.electionId = electionId;
        this.votes = votes;
        this.winner = winner;
    }

    public int getVotesByParty(PoliticalParty party){
        return this.votes[party.value];
    }

    public int getTotalVotes(){
        return Arrays.stream(votes).sum();
    }

    public int[] getVotesCopy() {
        int[] votesCopy = new int[this.votes.length];
        System.arraycopy(this.votes, 0, votesCopy, 0, this.votes.length);
        return votesCopy;
    }

    public static ElectionData[] combineElectionDataArr(ElectionData[] e1, ElectionData[] e2)
            throws MismatchedElectionException {
        ElectionType[] electionTypes = ElectionType.values();
        ElectionData[] newElectionDataArr = new ElectionData[electionTypes.length];
        for (ElectionType electionId : electionTypes){
            int index = electionId.value;
            newElectionDataArr[index] = ElectionData.combine(e1[index], e2[index]);
        }
        return newElectionDataArr;
    }

    public static ElectionData combine(ElectionData e1, ElectionData e2) throws MismatchedElectionException {
        if (e1.electionId != e2.electionId){
            throw new MismatchedElectionException("Replace this string later.");
        }

        int[] e1Votes = e1.getVotesCopy();
        int[] e2Votes = e2.getVotesCopy();

        PoliticalParty[] partyTypes = PoliticalParty.values();
        int[] combinedVotes = new int[partyTypes.length];
        Stream.of(partyTypes).forEach(val -> {
            int index = val.value;
            combinedVotes[index] = e1Votes[index] + e2Votes[index];
        });

        int max = Arrays.stream(combinedVotes).max().getAsInt();
        long maxOccurrences = Arrays.stream(combinedVotes).filter(v -> v == max).count();
        int winnerOrdinal = -1;
        if (maxOccurrences == 1){
            winnerOrdinal = IntStream.range(0, combinedVotes.length)
                                     .filter(i -> combinedVotes[i] == max)
                                     .findFirst()
                                     .orElse(-1);
        }
        PoliticalParty winner = (winnerOrdinal == -1) ? PoliticalParty.TIE : partyTypes[winnerOrdinal];
        return new ElectionData(e1.electionId, combinedVotes, winner);
    }
}
