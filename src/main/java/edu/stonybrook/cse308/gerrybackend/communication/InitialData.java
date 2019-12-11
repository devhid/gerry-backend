package edu.stonybrook.cse308.gerrybackend.communication;

import edu.stonybrook.cse308.gerrybackend.data.reports.AggregateInfo;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

import java.util.*;

public class InitialData {

    @Getter
    private AggregateInfo stateInfo;
    @Getter
    private Map<String, AggregateInfo> districtInfo;

    public InitialData() {
        this.districtInfo = new HashMap<>();
    }

    public void populateStateInfo(final StateNode statePres16, final StateNode stateHouse16,
                               final StateNode stateHouse18) {
        this.stateInfo = new AggregateInfo(statePres16.getDemographicData(), statePres16.getElectionData(),
                stateHouse16.getElectionData(), stateHouse18.getElectionData());
    }

    public void populateDistrictInfo(final StateNode statePres16, final StateNode stateHouse16,
                                  final StateNode stateHouse18) {
        Set<DistrictNode> districtsPres16 = stateHouse16.getChildren();
    }

}
