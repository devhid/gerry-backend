package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.data.weights.*;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class PhaseTwoInputs extends AlgPhaseInputs {

    @Getter
    private String stateId;

    @Getter
    private Set<DemographicType> demographicTypes;

    @Getter
    private double upperBound;

    @Getter
    private double lowerBound;

    @Getter
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
    public Set<MeasureWeight> getWeights() {
        Set<MeasureWeight> weights = new HashSet<>();
        weights.add(this.compactnessWeight);
        weights.add(this.competitivenessWeight);
        weights.add(this.fairnessWeight);
        weights.add(this.popEqualityWeight);
        weights.add(this.popHomogeneityWeight);
        return weights;
    }

    @Override
    protected boolean isValid() {
        boolean valid = this.stateId != null;
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
