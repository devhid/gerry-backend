package edu.stonybrook.cse308.gerrybackend.controllers.mappings;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhaseZeroResult {

    @Getter
    private final Map<PoliticalParty, List<PrecinctBlocSummary>> precinctBlocs;

    public PhaseZeroResult(final Map<PoliticalParty, Map<DemographicType, PrecinctBlocSummary>> voteBlocSummaries) {
        this.precinctBlocs = this.transformResult(voteBlocSummaries);
    }

    private Map<PoliticalParty, List<PrecinctBlocSummary>> transformResult(final Map<PoliticalParty,
            Map<DemographicType, PrecinctBlocSummary>> voteBlocSummaries) {
        return voteBlocSummaries.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue().values())));
    }

}
