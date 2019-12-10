package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitiveness;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

import java.util.Set;

public interface PoliticalCompetitivenessMeasure {

    class MarginOfVictory {
        public static double computeCompetitivenessScore(DistrictNode district) {
            // TODO: revisit and check if this is appropriate for more than 1 winner
            PoliticalParty arbitraryWinner = district.getElectionData().getWinners().iterator().next();
            int winningVotes = district.getElectionData().getPartyVotes(arbitraryWinner);
            int losingVotes = district.getElectionData().getAllOtherPartyVotes(arbitraryWinner);
            return 1.0 - (((double) Math.abs(winningVotes - losingVotes)) / (winningVotes + losingVotes));
        }
    }

    static double computeCompetitivenessScore(PoliticalCompetitiveness measure, DistrictNode district) {
        double competitivenessScore;
        switch (measure) {
            case MARGIN_OF_VICTORY:
                competitivenessScore = MarginOfVictory.computeCompetitivenessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return competitivenessScore;
    }

}
