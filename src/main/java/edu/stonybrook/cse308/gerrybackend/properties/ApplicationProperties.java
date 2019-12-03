package edu.stonybrook.cse308.gerrybackend.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

public final class ApplicationProperties {

    @Getter
    private final PhaseOneProperties phaseOneProperties;

    @Getter
    private final PhaseTwoProperties phaseTwoProperties;

    @Autowired
    public ApplicationProperties(PhaseOneProperties phaseOneProperties, PhaseTwoProperties phaseTwoProperties) {
        this.phaseOneProperties = phaseOneProperties;
        this.phaseTwoProperties = phaseTwoProperties;
    }
}
