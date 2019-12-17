package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PoliticalFairness;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

public interface PoliticalFairnessMeasure {

    class EfficiencyGap {
        static double computeFairnessScore(StateNode state) {
            int inefficientWinningVotes = 0;
            int inefficientLosingVotes = 0;
            int totalVotes = 0;
            for (DistrictNode d : state.getChildren()) {
                // TODO: revisit and check if this is appropriate for more than 1 winner
                PoliticalParty arbitraryWinner = d.getElectionData().getWinners().iterator().next();
                int winningVotes = d.getElectionData().getPartyVotes(arbitraryWinner);
                int losingVotes = d.getElectionData().getAllOtherPartyVotes(arbitraryWinner);
                inefficientWinningVotes += (winningVotes - losingVotes);
                inefficientLosingVotes += losingVotes;
                totalVotes += (winningVotes + losingVotes);
            }
            return 1.0 - ((Math.abs(inefficientWinningVotes - inefficientLosingVotes) * 1.0) / totalVotes);
        }

        static double computeFairnessScore(DistrictNode district) {
            int inefficientWinningVotes = 0;
            int inefficientLosingVotes = 0;
            int totalVotes = 0;
            PoliticalParty arbitraryWinner = district.getElectionData().getWinners().iterator().next();
            int winningVotes = district.getElectionData().getPartyVotes(arbitraryWinner);
            int losingVotes = district.getElectionData().getAllOtherPartyVotes(arbitraryWinner);
            inefficientWinningVotes += (winningVotes - losingVotes);
            inefficientLosingVotes += losingVotes;
            totalVotes += (winningVotes + losingVotes);
            return 1.0 - ((Math.abs(inefficientWinningVotes - inefficientLosingVotes) * 1.0) / totalVotes);
        }
    }

    class Gerrymander {
        static double computeFairnessScore(DistrictNode district, PoliticalParty party) {
            if (party == PoliticalParty.getDefault()) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            int partyVotes = district.getElectionData().getPartyVotes(party);
            int allOtherVotes = district.getElectionData().getAllOtherPartyVotes(party);
            int totalVotes = partyVotes + allOtherVotes;
            if (totalVotes == 0) {
                return 1.0;
            }
            int winningVotes = Math.max(partyVotes, allOtherVotes);
            int losingVotes = Math.min(partyVotes, allOtherVotes);
            boolean wastedConditional = (partyVotes > allOtherVotes);
            int wastedVotes = (wastedConditional) ? (winningVotes - losingVotes) : losingVotes;
            return 1.0 - ((wastedVotes * 1.0) / totalVotes);
        }
    }

    class GerrymanderDemocrat {
        static double computeFairnessScore(DistrictNode district) {
            return Gerrymander.computeFairnessScore(district, PoliticalParty.DEMOCRATIC);
        }
    }

    class GerrymanderRepublican {
        static double computeFairnessScore(DistrictNode district) {
            return Gerrymander.computeFairnessScore(district, PoliticalParty.REPUBLICAN);
        }
    }

    class LopsidedMargins {
        static double computeFairnessScore(DistrictNode district) {
            // TODO: fill in
            // reference http://www.stanfordlawreview.org/wp-content/uploads/sites/3/2016/06/3_-_Wang_-_Stan._L._Rev.pdf
            return -1.0;
        }
    }

    class MeanMedian {
        static double computeFairnessScore(StateNode state) {
            // TODO: fill in
            // reference http://www.stanfordlawreview.org/wp-content/uploads/sites/3/2016/06/3_-_Wang_-_Stan._L._Rev.pdf
            return -1.0;
        }
    }

    class Partisan {
        static double computeFairnessScore(DistrictNode district, PoliticalParty party) {
            int totalVotes = 0;
            int totalPartyVotes = 0;
            int totalPartyDistricts = 0;
            StateNode state = district.getParent();
            for (DistrictNode d : state.getChildren()) {
                totalVotes += d.getElectionData().getTotalVotes();
                totalPartyVotes += d.getElectionData().getPartyVotes(party);
                if (d.getElectionData().getWinners().contains(party)) {
                    totalPartyDistricts += 1;
                }
            }
            int totalDistricts = state.getChildren().size();
            int idealDistricts = (int) (Math.round(totalDistricts * (((double) totalPartyVotes) / totalVotes)));
            if (idealDistricts == totalPartyDistricts) {
                return 1.0;
            }
            return Gerrymander.computeFairnessScore(district, party);
        }
    }

    class PartisanDemocrat {
        static double computeFairnessScore(DistrictNode district) {
            return Partisan.computeFairnessScore(district, PoliticalParty.DEMOCRATIC);
        }
    }

    class PartisanRepublican {
        static double computeFairnessScore(DistrictNode district) {
            return Partisan.computeFairnessScore(district, PoliticalParty.REPUBLICAN);
        }
    }

    static double computeFairnessScore(PoliticalFairness measure, DistrictNode district) {
        double fairnessScore;
        switch (measure) {
            case EFFICIENCY_GAP:
                fairnessScore = EfficiencyGap.computeFairnessScore(district);
                break;
            case GERRYMANDER_DEMOCRAT:
                fairnessScore = GerrymanderDemocrat.computeFairnessScore(district);
                break;
            case GERRYMANDER_REPUBLICAN:
                fairnessScore = GerrymanderRepublican.computeFairnessScore(district);
                break;
            case LOPSIDED_MARGINS:
                fairnessScore = LopsidedMargins.computeFairnessScore(district);
                break;
            case PARTISAN_DEMOCRAT:
                fairnessScore = PartisanDemocrat.computeFairnessScore(district);
                break;
            case PARTISAN_REPUBLICAN:
                fairnessScore = PartisanRepublican.computeFairnessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return fairnessScore;
    }

    static double computeFairnessScore(PoliticalFairness measure, StateNode state) {
        double fairnessScore;
        switch (measure) {
            case EFFICIENCY_GAP:
                fairnessScore = EfficiencyGap.computeFairnessScore(state);
                break;
            case MEAN_MEDIAN:
                fairnessScore = MeanMedian.computeFairnessScore(state);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return fairnessScore;
    }

}
