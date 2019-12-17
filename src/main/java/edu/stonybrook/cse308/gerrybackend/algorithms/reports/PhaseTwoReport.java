package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.communication.dto.scores.DistrictScores;
import edu.stonybrook.cse308.gerrybackend.communication.dto.scores.StateScores;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Queue;

public class PhaseTwoReport extends IterativeAlgPhaseReport<PhaseTwoMoveDelta, PhaseTwoLogBuilder> {

    @Getter
    @Setter
    private Map<String, DistrictScores> newDistrictScores;

    @Getter
    @Setter
    private StateScores newStateScores;

    @Getter
    @Setter
    private Map<String, DistrictScores> oldDistrictScores;

    @Getter
    @Setter
    private StateScores oldStateScores;


    public PhaseTwoReport(StatusCode statusCode, String jobId, Queue<PhaseTwoMoveDelta> deltas, PhaseTwoLogBuilder logBuilder,
                          Map<String, DistrictScores> newDistrictScores, StateScores newStateScores,
                          Map<String, DistrictScores> oldDistrictScores, StateScores oldStateScores) {
        super(statusCode, jobId, deltas, logBuilder);
        this.newDistrictScores = newDistrictScores;
        this.newStateScores = newStateScores;
        this.oldDistrictScores = oldDistrictScores;
        this.oldStateScores = oldStateScores;
    }

    @Override
    protected IterativeAlgPhaseReport createNextReportFromDeltas(Queue<PhaseTwoMoveDelta> deltas) {
        return new PhaseTwoReport(this.statusCode, this.jobId, deltas, this.logBuilder,
                this.newDistrictScores, this.newStateScores, this.oldDistrictScores, this.oldStateScores);
    }

    public PhaseTwoReport fetchNextReport(int num) {
        IterativeAlgPhaseReport nextReport = super.fetchNextReport(num);
        return (PhaseTwoReport) nextReport;
    }

}
