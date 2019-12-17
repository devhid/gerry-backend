package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.data.weights.*;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureInterface;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PhaseTwoInputs extends AlgPhaseInputs {

    @Getter
    private String jobId;

    @Getter
    @Setter
    private Job job;

    @Getter
    private Set<DemographicType> demographicTypes;

    @Getter
    private double upperBound;

    @Getter
    private double lowerBound;

    @Getter
    @Setter
    private int numRetries;

    @Getter
    private CompactnessWeight compactnessWeight;

    @Getter
    private PoliticalCompetitivenessWeight competitivenessWeight;

    @Getter
    private PoliticalFairnessWeight fairnessWeight;

    @Getter
    private PopulationEqualityWeight popEqualityWeight;

    @Getter
    private PopulationHomogeneityWeight popHomogeneityWeight;

    @Getter
    private PhaseTwoDepth depthHeuristic;

    @Getter
    private PhaseTwoPrecinctMove moveHeuristic;

    @Getter
    @Setter
    private double epsilon;

    @JsonIgnore
    private Map<MeasureInterface, Double> weightMap;

    @JsonIgnore
    public Set<MeasureWeight> getWeights() {
        Set<MeasureWeight> weights = new HashSet<>();
        if (this.compactnessWeight.getWeight() > 0.0) {
            weights.add(this.compactnessWeight);
        }
        if (this.competitivenessWeight.getWeight() > 0.0) {
            weights.add(this.competitivenessWeight);
        }
        if (this.fairnessWeight.getWeight() > 0.0) {
            weights.add(this.fairnessWeight);
        }
        if (this.popEqualityWeight.getWeight() > 0.0) {
            weights.add(this.popEqualityWeight);
        }
        if (this.popHomogeneityWeight.getWeight() > 0.0) {
            weights.add(this.popHomogeneityWeight);
        }
        return weights;
    }

    @JsonIgnore
    public Map<MeasureInterface, Double> getWeightMap() {
        if(this.weightMap == null) {
            this.weightMap = new HashMap<>();

            for(MeasureWeight weight: this.getWeights()) {
                this.weightMap.put(weight.getMeasure(), weight.getWeight());
            }
        }

        return weightMap;
    }

    @Override
    protected boolean isValid() {
        boolean valid = this.jobId != null;
        valid = valid && (this.demographicTypes != null) && (this.demographicTypes.size() > 0);
        valid = valid && (this.upperBound > this.lowerBound);
        valid = valid && (this.numRetries > 0);
        for (MeasureWeight weight : this.getWeights()) {
            valid = valid && (weight.isValid());
        }
        valid = valid && (this.depthHeuristic != null);
        valid = valid && (this.moveHeuristic != null);
        return valid;
    }

}
