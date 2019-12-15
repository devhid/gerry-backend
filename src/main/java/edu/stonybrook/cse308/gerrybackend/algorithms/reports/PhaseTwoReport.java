package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;

import java.util.Queue;

public class PhaseTwoReport extends IterativeAlgPhaseReport<PhaseTwoMoveDelta, PhaseTwoLogBuilder> {

    public PhaseTwoReport(StatusCode statusCode, Queue<PhaseTwoMoveDelta> deltas, PhaseTwoLogBuilder logBuilder) {
        super(statusCode, deltas, logBuilder);
    }

    @Override
    protected IterativeAlgPhaseReport createNextReportFromDeltas(Queue<PhaseTwoMoveDelta> deltas) {
        return new PhaseTwoReport(this.statusCode, deltas, this.logBuilder);
    }

    public PhaseTwoReport fetchNextReport(int num) {
        IterativeAlgPhaseReport nextReport = super.fetchNextReport(num);
        return (PhaseTwoReport) nextReport;
    }

}
