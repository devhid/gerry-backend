package edu.stonybrook.cse308.gerrybackend.initializers;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.communication.dto.scores.DistrictScores;
import edu.stonybrook.cse308.gerrybackend.communication.dto.scores.StateScores;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;

@Component
public class PhaseTwoReportInitializer {

    @Getter
    private static PhaseTwoLogBuilder logBuilder;

    @Autowired
    public void setPhaseOneLogBuilder(PhaseTwoLogBuilder logBuilder) {
        PhaseTwoReportInitializer.logBuilder = logBuilder;
    }

    public static PhaseTwoReport initClass(StatusCode statusCode, String jobId, Queue<PhaseTwoMoveDelta> deltas,
                                           Map<String, DistrictScores> newDistrictScores, StateScores newStateScores,
                                           Map<String, DistrictScores> oldDistrictScores, StateScores oldStateScores) {
        return new PhaseTwoReport(statusCode, jobId, deltas, PhaseTwoReportInitializer.logBuilder,
                newDistrictScores, newStateScores, oldDistrictScores, oldStateScores);
    }

}
