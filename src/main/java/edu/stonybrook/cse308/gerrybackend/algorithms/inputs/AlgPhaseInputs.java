package edu.stonybrook.cse308.gerrybackend.algorithms.inputs;

import edu.stonybrook.cse308.gerrybackend.enums.types.AlgRunType;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

public abstract class AlgPhaseInputs {

    @Getter
    protected StateType stateType;

    @Getter
    protected ElectionType electionType;

    @Getter
    protected StateNode state;

}
