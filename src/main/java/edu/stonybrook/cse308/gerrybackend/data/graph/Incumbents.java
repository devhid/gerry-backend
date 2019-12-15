package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.enums.converters.PoliticalPartyConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import lombok.Getter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class Incumbents {

    @Getter
    private StateType stateType;

    @Getter
    private Map<String, Incumbent> incumbentMap;

    public Incumbents() {
        this.stateType = StateType.NOT_SET;
        this.incumbentMap = new HashMap<String, Incumbent>();
    }

    public Incumbents(StateType stateType, Map<String, Incumbent> incumbentMap) {
        this.stateType = stateType;
        this.incumbentMap = incumbentMap;
    }
}
