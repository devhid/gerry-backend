package edu.stonybrook.cse308.gerrybackend.data.graph;

import edu.stonybrook.cse308.gerrybackend.enums.converters.ElectionTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.converters.PoliticalPartyConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import lombok.Getter;

import javax.persistence.*;
import java.util.UUID;

@Embeddable
public class Incumbent {

    @Getter
    @Id
    @Column(name="id")
    private String id;

    @Getter
    @Column(name="name")
    private String name;

    @Getter
    @Convert(converter=PoliticalPartyConverter.class)
    private PoliticalParty party;

    public Incumbent() {
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.party = PoliticalParty.NOT_SET;
    }

    public Incumbent(String id, String name, PoliticalParty party) {
        this.id = id;
        this.name = name;
        this.party = party;
    }
}
