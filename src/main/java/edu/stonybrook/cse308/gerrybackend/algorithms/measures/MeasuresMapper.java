package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.algorithms.measures.compactness.GraphTheoreticalCompactness;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.compactness.PolsbyPopperCompactness;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.compactness.SchwartzbergCompactness;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.political.competitiveness.MarginOfVictoryCompetitiveness;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.political.fairness.*;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.population.equality.MostToLeastPopulationEquality;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.population.homogeneity.NormalizedSquareErrorPopulationHomogeneity;
import edu.stonybrook.cse308.gerrybackend.enums.measures.*;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class MeasuresMapper {

    private static Map<CompactnessEnum, Function<StateNode,Double>> compactnessMeasures;
    private static Map<PoliticalCompetitivenessEnum, Function<StateNode,Double>> competitivenessMeasures;
    private static Map<PoliticalFairnessEnum, Function<StateNode,Double>> fairnessMeasures;
    private static Map<PopulationEqualityEnum, Function<StateNode,Double>> popEqualityMeasures;
    private static Map<PopulationHomogeneityEnum, Function<StateNode, Double>> popHomogeneityMeasures;
    static {
        // Compactness
        compactnessMeasures = new EnumMap<>(CompactnessEnum.class);
        compactnessMeasures.put(CompactnessEnum.GRAPH_THEORETICAL, GraphTheoreticalCompactness::computeCompactnessScore);
        compactnessMeasures.put(CompactnessEnum.POLSBY_POPPER, PolsbyPopperCompactness::computeCompactnessScore);
        compactnessMeasures.put(CompactnessEnum.SCHWARTZBERG, SchwartzbergCompactness::computeCompactnessScore);

        // Competitiveness
        competitivenessMeasures = new EnumMap<>(PoliticalCompetitivenessEnum.class);
        competitivenessMeasures.put(PoliticalCompetitivenessEnum.MARGIN_OF_VICTORY, MarginOfVictoryCompetitiveness::computeCompetitivenessScore);

        // Fairness
        fairnessMeasures = new EnumMap<>(PoliticalFairnessEnum.class);
        fairnessMeasures.put(PoliticalFairnessEnum.EFFICIENCY_GAP, EfficiencyGapFairness::computeFairnessScore);
        fairnessMeasures.put(PoliticalFairnessEnum.GERRYMANDER_DEMOCRAT, GerrymanderDemocratFairness::computeFairnessScore);
        fairnessMeasures.put(PoliticalFairnessEnum.GERRYMANDER_REPUBLICAN, GerrymanderRepublicanFairness::computeFairnessScore);
        fairnessMeasures.put(PoliticalFairnessEnum.LOPSIDED_MARGINS, LopsidedMarginsFairness::computeFairnessScore);
        fairnessMeasures.put(PoliticalFairnessEnum.MEAN_MEDIAN, MeanMedianFairness::computeFairnessScore);
        fairnessMeasures.put(PoliticalFairnessEnum.PARTISAN, PartisanFairness::computeFairnessScore);

        // Population Equality
        popEqualityMeasures = new EnumMap<>(PopulationEqualityEnum.class);
        popEqualityMeasures.put(PopulationEqualityEnum.MOST_TO_LEAST, MostToLeastPopulationEquality::computePopulationEqualityScore);

        // Population Homogeneity
        popHomogeneityMeasures = new EnumMap<>(PopulationHomogeneityEnum.class);
        popHomogeneityMeasures.put(PopulationHomogeneityEnum.NORMALIZED_SQUARE_ERROR, NormalizedSquareErrorPopulationHomogeneity::computePopulationHomogeneityScore);
    }

    public static double computeScore(MeasureEnumInterface measure, StateNode state){
        if (measure instanceof CompactnessEnum){
            return computeCompactnessScore((CompactnessEnum) measure, state);
        }
        else if (measure instanceof PoliticalCompetitivenessEnum){
            return computeCompetitivenessScore((PoliticalCompetitivenessEnum) measure, state);
        }
        else if (measure instanceof PoliticalFairnessEnum){
            return computeFairnessScore((PoliticalFairnessEnum) measure, state);
        }
        else if (measure instanceof PopulationEqualityEnum){
            return computePopulationEqualityScore((PopulationEqualityEnum) measure, state);
        }
        else if (measure instanceof PopulationHomogeneityEnum){
            return computePopulationHomogeneityScore((PopulationHomogeneityEnum) measure, state);
        }
        throw new IllegalArgumentException("Replace this string later!");
    }

    public static double computeCompactnessScore(CompactnessEnum measure, StateNode state){
        return compactnessMeasures.get(measure).apply(state);
    }

    public static double computeCompetitivenessScore(PoliticalCompetitivenessEnum measure, StateNode state){
        return competitivenessMeasures.get(measure).apply(state);
    }

    public static double computeFairnessScore(PoliticalFairnessEnum measure, StateNode state){
        return fairnessMeasures.get(measure).apply(state);
    }

    public static double computePopulationEqualityScore(PopulationEqualityEnum measure, StateNode state){
        return popEqualityMeasures.get(measure).apply(state);
    }

    public static double computePopulationHomogeneityScore(PopulationHomogeneityEnum measure, StateNode state){
        return popHomogeneityMeasures.get(measure).apply(state);
    }

}
