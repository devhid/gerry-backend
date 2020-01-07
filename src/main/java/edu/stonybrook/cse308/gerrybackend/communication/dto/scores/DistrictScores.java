package edu.stonybrook.cse308.gerrybackend.communication.dto.scores;

import edu.stonybrook.cse308.gerrybackend.data.measures.scores.*;
import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
public class DistrictScores {

    @Getter
    @Setter
    protected CompactnessScore compactness;

    @Getter
    @Setter
    protected PoliticalCompetitivenessScore competitiveness;

    @Getter
    @Setter
    protected PoliticalFairnessScore fairness;

    @Getter
    @Setter
    protected PopulationEqualityScore populationEquality;

    @Getter
    @Setter
    protected PopulationHomogeneityScore populationHomogeneity;

    public DistrictScores() {

    }

    public double getSum() {
        double sum = 0.0;
        sum += (this.compactness != null) ? this.compactness.getScore() : 0;
        sum += (this.competitiveness != null) ? this.competitiveness.getScore() : 0;
        sum += (this.fairness != null) ? this.fairness.getScore() : 0;
        sum += (this.populationEquality != null) ? this.populationEquality.getScore() : 0;
        sum += (this.populationHomogeneity != null) ? this.populationHomogeneity.getScore() : 0;
        return sum;
    }

    public static DistrictScores fromScoreMap(Map<MeasureInterface, Double> scoreMap) {
        DistrictScores districtScores = new DistrictScores();
        scoreMap.forEach((measure, score) -> {
            if (measure instanceof Compactness) {
                CompactnessScore compactnessScore = new CompactnessScore((Compactness) measure, score);
                districtScores.setCompactness(compactnessScore);
            } else if (measure instanceof PoliticalCompetitiveness) {
                PoliticalCompetitivenessScore competitivenessScore =
                        new PoliticalCompetitivenessScore((PoliticalCompetitiveness) measure, score);
                districtScores.setCompetitiveness(competitivenessScore);
            } else if (measure instanceof PoliticalFairness) {
                PoliticalFairnessScore fairnessScore =
                        new PoliticalFairnessScore((PoliticalFairness) measure, score);
                districtScores.setFairness(fairnessScore);
            } else if (measure instanceof PopulationEquality) {
                PopulationEqualityScore populationEqualityScore =
                        new PopulationEqualityScore((PopulationEquality) measure, score);
                districtScores.setPopulationEquality(populationEqualityScore);
            } else if (measure instanceof PopulationHomogeneity) {
                PopulationHomogeneityScore populationHomogeneityScore =
                        new PopulationHomogeneityScore((PopulationHomogeneity) measure, score);
                districtScores.setPopulationHomogeneity(populationHomogeneityScore);
            }
        });
        return districtScores;
    }

}
