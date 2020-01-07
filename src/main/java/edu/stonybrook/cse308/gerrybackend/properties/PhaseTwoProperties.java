package edu.stonybrook.cse308.gerrybackend.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class PhaseTwoProperties {

    @Getter
    private final String deltaTemplate;

    public PhaseTwoProperties(@Value("${gerry.templates.logs.phasetwodelta}") String deltaTemplate) {
        this.deltaTemplate = deltaTemplate;
    }
}
