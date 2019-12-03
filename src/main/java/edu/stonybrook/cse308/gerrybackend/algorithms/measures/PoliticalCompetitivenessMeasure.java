package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalCompetitiveness;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;

public interface PoliticalCompetitivenessMeasure {

    class MarginOfVictory {
        public static double computeCompetitivenessScore(DistrictNode district) {
            PoliticalParty winner = district.getElectionData().getWinner();
            int winningVotes = district.getElectionData().getPartyVotes(winner);
            int losingVotes = district.getElectionData().getAllOtherPartyVotes(winner);
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
