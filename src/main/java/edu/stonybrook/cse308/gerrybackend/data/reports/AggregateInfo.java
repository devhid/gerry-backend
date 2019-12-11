package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

public class AggregateInfo {

    @Getter
    private final DemographicData demographicData;
    @Getter
    private final ElectionData pres16ElectionData;
    @Getter
    private final ElectionData house16ElectionData;
    @Getter
    private final ElectionData house18ElectionData;

    public AggregateInfo(final DemographicData demographicData, ElectionData pres16ElectionData,
                         ElectionData house16ElectionData, ElectionData house18ElectionData) {
        this.demographicData = demographicData;
        this.pres16ElectionData = pres16ElectionData;
        this.house16ElectionData = house16ElectionData;
        this.house18ElectionData = house18ElectionData;
    }

}
