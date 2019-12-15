package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import lombok.Getter;
import lombok.Setter;

public abstract class AlgPhaseReport {

    @Getter
    @Setter
    protected StatusCode statusCode;

    protected AlgPhaseReport(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

}
