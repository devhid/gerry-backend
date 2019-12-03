package edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.IterativeAlgPhaseLog;
import edu.stonybrook.cse308.gerrybackend.data.reports.IterativeAlgPhaseDelta;

/**
 * A contract for building an IterativePhaseLog from a delta of type D that extends IterativeAlgPhaseDelta.
 *
 * @param <D> the type of delta that is used as the source for the log
 */
public interface IterativeAlgPhaseLogBuilder<D extends IterativeAlgPhaseDelta> {

    IterativeAlgPhaseLog buildLogFromDelta(D delta);

}
