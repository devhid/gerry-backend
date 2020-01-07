package edu.stonybrook.cse308.gerrybackend.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class PhaseOneProperties {

    @Getter
    private final String deltaAbsorbSeparator;

    @Getter
    private final String deltaAbsorbTemplate;

    @Getter
    private final String deltaTemplate;

    @Autowired
    public PhaseOneProperties(
            @Value("${gerry.templates.logs.phaseonedelta.absorb.sep}") String deltaAbsorbSeparator,
            @Value("${gerry.templates.logs.phaseonedelta.absorb}") String deltaAbsorbTemplate,
            @Value("${gerry.templates.logs.phaseonedelta}") String deltaTemplate) {
        this.deltaAbsorbSeparator = deltaAbsorbSeparator;
        this.deltaAbsorbTemplate = deltaAbsorbTemplate;
        this.deltaTemplate = deltaTemplate;
    }
}
