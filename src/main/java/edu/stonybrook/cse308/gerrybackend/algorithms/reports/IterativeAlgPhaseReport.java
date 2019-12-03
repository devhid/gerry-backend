package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.IterativeAlgPhaseLogBuilder;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.IterativeAlgPhaseLog;
import edu.stonybrook.cse308.gerrybackend.data.reports.IterativeAlgPhaseDelta;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class IterativeAlgPhaseReport<D extends IterativeAlgPhaseDelta, B extends IterativeAlgPhaseLogBuilder<D>> extends AlgPhaseReport {

    @Getter
    protected String newStateId;

    @Getter
    protected Queue<D> deltas;

    @Getter
    @JsonIgnore
    protected B logBuilder;

    IterativeAlgPhaseReport(String newStateId, Queue<D> deltas, B logBuilder) {
        this.newStateId = newStateId;
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
