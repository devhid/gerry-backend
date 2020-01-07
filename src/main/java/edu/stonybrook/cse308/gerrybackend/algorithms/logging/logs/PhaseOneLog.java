package edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs;

public class PhaseOneLog extends IterativeAlgPhaseLog {

    private String absorbedNodes;

    public PhaseOneLog(String template, int iteration, String absorbedNodes) {
        super(template, iteration);
        this.absorbedNodes = absorbedNodes;
    }

    @Override
    public String toString() {
        return String.format(this.template, this.iteration, this.absorbedNodes);
    }

}
