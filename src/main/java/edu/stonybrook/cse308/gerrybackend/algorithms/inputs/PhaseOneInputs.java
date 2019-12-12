package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgRunType;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PhaseOneInputs extends AlgPhaseInputs {

    @Getter
    private int numDistricts;

    @Getter
    private Set<DemographicType> demographicTypes;

    @Getter
    private double upperBound;

    @Getter
    private double lowerBound;

    @Getter
    private AlgRunType algRunType;

    @Getter
    private PhaseOneMajMinPairs majMinPairsHeuristic;

    @Getter
    private PhaseOneOtherPairs otherPairsHeuristic;

    @Getter
    private PhaseOneStop stopHeuristic;

    @Override
    protected boolean isValid() {
        boolean valid = super.isValid();
        valid = valid && (this.demographicTypes != null) && (this.demographicTypes.size() > 0);
        valid = valid && (this.upperBound > this.lowerBound);
        valid = valid && (this.algRunType != null);
        valid = valid && (this.majMinPairsHeuristic != null);
        valid = valid && (this.otherPairsHeuristic != null);
        valid = valid && (this.stopHeuristic != null);
        return true;
    }

    public static PhaseOneInputs fromString(final String json) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        PhaseOneInputs inputs = objectMapper.readValue(json, PhaseOneInputs.class);
        return inputs;
    }
}
