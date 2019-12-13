package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone.MergedDistrict;
import lombok.Getter;

import java.util.Map;

public class PhaseOneMergeDelta extends IterativeAlgPhaseDelta {

    @Getter
    private Map<String, String> changedNodes;

    @Getter
    private Map<String, MergedDistrict> newDistricts;

    public PhaseOneMergeDelta(int iteration, Map<String, String> changedNodes, Map<String, MergedDistrict> newDistricts) {
        super(iteration);
        this.changedNodes = changedNodes;
        this.newDistricts = newDistricts;
    }

}
