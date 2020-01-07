package edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs;

public class PhaseTwoLog extends IterativeAlgPhaseLog {

    private String precinctId;
    private String oldDistrictId;
    private String newDistrictId;

    public PhaseTwoLog(String template, int iteration, String precinctId, String oldDistrictId, String newDistrictId) {
        super(template, iteration);
        this.precinctId = precinctId;
        this.oldDistrictId = oldDistrictId;
        this.newDistrictId = newDistrictId;
    }

    @Override
    public String toString() {
        return String.format(this.template, this.iteration, this.precinctId, this.oldDistrictId, this.newDistrictId);
    }
}
