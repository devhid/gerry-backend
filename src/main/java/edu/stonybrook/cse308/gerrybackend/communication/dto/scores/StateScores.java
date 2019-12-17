package edu.stonybrook.cse308.gerrybackend.communication.dto.scores;

import edu.stonybrook.cse308.gerrybackend.algorithms.measures.PopulationEqualityMeasure;
import edu.stonybrook.cse308.gerrybackend.data.measures.scores.*;
import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class StateScores extends DistrictScores {

    @Getter
    private double mostToLeastPercentDifference;

    public StateScores(CompactnessScore compactness, PoliticalCompetitivenessScore competitiveness,
                       PoliticalFairnessScore fairness, PopulationEqualityScore populationEquality,
                       PopulationHomogeneityScore populationHomogeneity, double mostToLeastPercentDifference) {
        super(compactness, competitiveness, fairness, populationEquality, populationHomogeneity);
        this.mostToLeastPercentDifference = mostToLeastPercentDifference;
    }

    public static StateScores fromDistrictScoresAndState(Set<Map.Entry<DistrictNode, DistrictScores>> districtScores,
                                                         StateNode state) {
        Compactness compactnessMeasure = null;
        PoliticalCompetitiveness competitivenessMeasure = null;
        PoliticalFairness fairnessMeasure = null;
        PopulationEquality populationEqualityMeasure = null;
        PopulationHomogeneity populationHomogeneityMeasure = null;
        double compactness = 0.0;
        double competitiveness = 0.0;
        double fairness = 0.0;
        double populationEquality = 0.0;
        double populationHomogeneity = 0.0;
        for (Map.Entry<DistrictNode, DistrictScores> e : districtScores) {
            DistrictScores dScores = e.getValue();
            if (compactnessMeasure == null) {
                compactnessMeasure = (dScores.getCompactness() != null) ? dScores.getCompactness().getMeasure() : null;
            }
            if (competitivenessMeasure == null) {
                competitivenessMeasure = (dScores.getCompetitiveness() != null) ?
                        dScores.getCompetitiveness().getMeasure() : null;
            }
            if (fairnessMeasure == null) {
                fairnessMeasure = (dScores.getFairness() != null) ? dScores.getFairness().getMeasure() : null;
            }
            if (populationEqualityMeasure == null) {
                populationEqualityMeasure = (dScores.getPopulationEquality() != null) ?
                        dScores.getPopulationEquality().getMeasure() : null;
            }
            if (populationHomogeneityMeasure == null) {
                populationHomogeneityMeasure = (dScores.getPopulationHomogeneity() != null) ?
                        dScores.getPopulationHomogeneity().getMeasure() : null;
            }
            compactness += (dScores.getCompactness() != null) ? dScores.getCompactness().getScore() : 0;
            competitiveness += (dScores.getCompetitiveness() != null) ? dScores.getCompetitiveness().getScore() : 0;
            fairness += (dScores.getFairness() != null) ? dScores.getFairness().getScore() : 0;
            populationEquality += (dScores.getPopulationEquality() != null) ? dScores.getPopulationEquality().getScore() : 0;
            populationHomogeneity += (dScores.getPopulationHomogeneity() != null) ? dScores.getPopulationHomogeneity().getScore() : 0;
        }
        compactness /= districtScores.size();
        competitiveness /= districtScores.size();
        fairness /= districtScores.size();
        populationEquality /= districtScores.size();
        populationHomogeneity /= districtScores.size();

        CompactnessScore compactnessScore = new CompactnessScore(compactnessMeasure, compactness);
        PoliticalCompetitivenessScore competitivenessScore = new PoliticalCompetitivenessScore(competitivenessMeasure, competitiveness);
        PoliticalFairnessScore fairnessScore = new PoliticalFairnessScore(fairnessMeasure, fairness);
        PopulationEqualityScore populationEqualityScore = new PopulationEqualityScore(populationEqualityMeasure, populationEquality);
        PopulationHomogeneityScore populationHomogeneityScore = new PopulationHomogeneityScore(populationHomogeneityMeasure, populationHomogeneity);

        double mostToLeastPercentDifference = PopulationEqualityMeasure.computePopulationEqualityScore(PopulationEquality.MOST_TO_LEAST, state);
        return new StateScores(compactnessScore, competitivenessScore, fairnessScore, populationEqualityScore,
                populationHomogeneityScore, mostToLeastPercentDifference);
    }
}
