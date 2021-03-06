package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.IterativeAlgPhaseLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseTwoLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.IterativeAlgPhaseLog;
import edu.stonybrook.cse308.gerrybackend.data.reports.IterativeAlgPhaseDelta;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class IterativeAlgPhaseReport<D extends IterativeAlgPhaseDelta, B extends IterativeAlgPhaseLogBuilder<D>> extends AlgPhaseReport {

    @Getter
    @Setter
    protected String jobId;

    @Getter
    protected Queue<D> deltas;

    @Getter
    @JsonIgnore
    protected B logBuilder;

    IterativeAlgPhaseReport(StatusCode statusCode, String jobId, Queue<D> deltas, B logBuilder) {
        super(statusCode);
        this.jobId = jobId;
        this.deltas = deltas;
        this.logBuilder = logBuilder;
    }

    /**
     * Fetches the next batch of deltas.
     *
     * @param num number of deltas to fetch
     * @return a queue containing the next batch of deltas
     */
    private Queue<D> fetchNextDeltas(int num) {
        Queue<D> deltas = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            deltas.offer(this.deltas.poll());
        }
        return deltas;
    }

    protected abstract IterativeAlgPhaseReport createNextReportFromDeltas(Queue<D> deltas);

    protected IterativeAlgPhaseReport fetchNextReport(int num) {
        return this.createNextReportFromDeltas(this.fetchNextDeltas(num));
    }

    public List<IterativeAlgPhaseLog> getLogs() {
        List<IterativeAlgPhaseLog> logs = new ArrayList<>();
        for (D delta : deltas) {
            logs.add(this.logBuilder.buildLogFromDelta(delta));
        }
        return logs;
    }

}
