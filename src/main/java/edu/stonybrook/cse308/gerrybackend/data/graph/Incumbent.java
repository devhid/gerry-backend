package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.enums.converters.PoliticalPartyConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Embeddable
public class Incumbent {

    @Getter
    private String name;

    @Getter
    @Convert(converter = PoliticalPartyConverter.class)
    private PoliticalParty party;

    public Incumbent() {
        this.name = "";
        this.party = PoliticalParty.NOT_SET;
    }

    public Incumbent(String name, PoliticalParty party) {
        this.name = name;
        this.party = party;
    }
}
