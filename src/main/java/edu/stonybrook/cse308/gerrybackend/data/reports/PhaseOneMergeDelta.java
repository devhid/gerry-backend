package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone.MergedDistrict;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class PhaseOneMergeDelta extends IterativeAlgPhaseDelta {

    @Getter
    private Map<String, String> changedNodes;

    @Getter
    private Map<String, MergedDistrict> newDistricts;

    @Getter
    @Setter
    private int numMajMinDistricts;

    @Getter
    private int numDistricts;

    public PhaseOneMergeDelta(int iteration, Map<String, String> changedNodes, Map<String, MergedDistrict> newDistricts,
                              int numMajMinDistricts, int numDistricts) {
        super(iteration);
        this.changedNodes = changedNodes;
        this.newDistricts = newDistricts;
        this.numMajMinDistricts = numMajMinDistricts;
        this.numDistricts = numDistricts;
    }

}
