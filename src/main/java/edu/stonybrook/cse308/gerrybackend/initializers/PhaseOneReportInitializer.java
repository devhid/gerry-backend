package edu.stonybrook.cse308.gerrybackend.initializers;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseOneLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;

@Component
public class PhaseOneReportInitializer {

    @Getter
    private static PhaseOneLogBuilder logBuilder;

    @Autowired
    public void setPhaseOneLogBuilder(PhaseOneLogBuilder logBuilder) {
        PhaseOneReportInitializer.logBuilder = logBuilder;
    }

    public static PhaseOneReport initClass(String newStateId, Queue<PhaseOneMergeDelta> deltas) {
        return new PhaseOneReport(newStateId, deltas, PhaseOneReportInitializer.logBuilder);
    }

}
