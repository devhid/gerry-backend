package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;

import java.util.Queue;

public class PhaseTwoReport extends IterativeAlgPhaseReport<PhaseTwoMoveDelta, PhaseTwoLogBuilder> {

    public PhaseTwoReport(String newStateId, Queue<PhaseTwoMoveDelta> deltas, PhaseTwoLogBuilder logBuilder) {
        super(newStateId, deltas, logBuilder);
    }

    @Override
    protected IterativeAlgPhaseReport createNextReportFromDeltas(Queue<PhaseTwoMoveDelta> deltas) {
        return new PhaseTwoReport(this.newStateId, deltas, this.logBuilder);
    }

    public PhaseTwoReport fetchNextReport(int num) {
        IterativeAlgPhaseReport nextReport = super.fetchNextReport(num);
        return (PhaseTwoReport) nextReport;
    }

}
