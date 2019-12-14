package edu.stonybrook.cse308.gerrybackend.initializers;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseOneLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.Set;

@Component
public class PhaseOneReportInitializer {

    private static PhaseOneLogBuilder logBuilder;

    @Autowired
    public void setPhaseOneLogBuilder(PhaseOneLogBuilder logBuilder) {
        PhaseOneReportInitializer.logBuilder = logBuilder;
    }

    public static PhaseOneReport initClass(Queue<PhaseOneMergeDelta> deltas, String jobId,
                                           Set<DistrictNode> remnantDistricts) {
        return new PhaseOneReport(deltas, PhaseOneReportInitializer.logBuilder, jobId, remnantDistricts);
    }

}
