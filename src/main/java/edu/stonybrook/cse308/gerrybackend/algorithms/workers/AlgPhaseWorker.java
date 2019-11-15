package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.AlgPhaseReport;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;

public abstract class AlgPhaseWorker<I extends AlgPhaseInputs, R extends AlgPhaseReport> {

    private String id;
    private AlgPhaseType algPhaseType;

    public abstract R run(I inputs);

}
