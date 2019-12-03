package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.PopulationHomogeneity;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;

public interface PopulationHomogeneityMeasure {

    class NormalizedSquareError {
        public static double computePopulationHomogeneityScore(DistrictNode district) {
            if (district.getChildren().size() == 0)
                return 0;
            double sum = district.getChildren().stream().mapToDouble(GerryNode::getPopulationDensity).sum();
            final double mean = sum / district.getChildren().size();
            double sqError = district.getChildren().stream()
                    .mapToDouble((precinct) -> (Math.pow(precinct.getPopulationDensity() - mean, 2)))
                    .sum();
            sqError /= (district.getChildren().size());
            // TODO: figure out why this was in the annealing 2 code
            // avg population density in km^2
            // double averagePopulationDensity = 3000;
            return 1.0 - Math.tanh(Math.sqrt(sqError / mean) / (mean));
        }
    }

    static double computePopulationHomogeneityScore(PopulationHomogeneity measure, DistrictNode district) {
        double popHomogeneityScore = 0.0;
        switch (measure) {
            case NORMALIZED_SQUARE_ERROR:
                popHomogeneityScore = NormalizedSquareError.computePopulationHomogeneityScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return popHomogeneityScore;
    }

}