package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;

import java.util.Queue;

public class PhaseTwoReport extends IterativeAlgPhaseReport<PhaseTwoMoveDelta, PhaseTwoLogBuilder> {

    @Getter
    private String jobId;

    public PhaseTwoReport(StatusCode statusCode, Queue<PhaseTwoMoveDelta> deltas, PhaseTwoLogBuilder logBuilder, String jobId) {
        super(statusCode, deltas, logBuilder);
        this.jobId = jobId;
    }

    @Override
    protected IterativeAlgPhaseReport createNextReportFromDeltas(Queue<PhaseTwoMoveDelta> deltas) {
        return new PhaseTwoReport(this.statusCode, deltas, this.logBuilder, this.jobId);
    }

    public PhaseTwoReport fetchNextReport(int num) {
        IterativeAlgPhaseReport nextReport = super.fetchNextReport(num);
        return (PhaseTwoReport) nextReport;
    }

}
