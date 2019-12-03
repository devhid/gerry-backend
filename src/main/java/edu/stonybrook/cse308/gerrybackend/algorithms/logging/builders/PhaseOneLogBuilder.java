package edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders;

import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.IterativeAlgPhaseLog;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.logs.PhaseOneLog;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.properties.PhaseOneProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhaseOneLogBuilder implements IterativeAlgPhaseLogBuilder<PhaseOneMergeDelta> {

    private PhaseOneProperties phaseOneProperties;

    @Autowired
    public PhaseOneLogBuilder(PhaseOneProperties phaseOneProperties) {
        this.phaseOneProperties = phaseOneProperties;
    }

    @Override
    public IterativeAlgPhaseLog buildLogFromDelta(PhaseOneMergeDelta delta) {
        final StringBuilder absorbedNodes = new StringBuilder();
        final String absorbTemplate = this.phaseOneProperties.getDeltaAbsorbTemplate();
        final String absorbSeparator = this.phaseOneProperties.getDeltaAbsorbSeparator();
        delta.getChangedNodes().forEach((smallerNodeId, largerNodeId) -> {
            absorbedNodes.append(String.format(absorbTemplate, smallerNodeId, largerNodeId));
            absorbedNodes.append(absorbSeparator);
        });
        absorbedNodes.setLength(absorbedNodes.length() - 1);
        return new PhaseOneLog(phaseOneProperties.getDeltaTemplate(), delta.getIteration(), absorbedNodes.toString());
    }

}
