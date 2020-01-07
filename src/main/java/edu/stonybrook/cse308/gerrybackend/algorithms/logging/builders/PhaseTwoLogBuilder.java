package edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.IterativeAlgPhaseLog;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.PhaseTwoLog;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.properties.PhaseTwoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhaseTwoLogBuilder implements IterativeAlgPhaseLogBuilder<PhaseTwoMoveDelta> {

    private final PhaseTwoProperties phaseTwoProperties;

    @Autowired
    public PhaseTwoLogBuilder(PhaseTwoProperties phaseTwoProperties) {
        this.phaseTwoProperties = phaseTwoProperties;
    }

    @Override
    public IterativeAlgPhaseLog buildLogFromDelta(PhaseTwoMoveDelta delta) {
        return new PhaseTwoLog(phaseTwoProperties.getDeltaTemplate(), delta.getIteration(),
                delta.getMovedPrecinctId(), delta.getOldDistrictId(), delta.getNewDistrictId());
    }

}
