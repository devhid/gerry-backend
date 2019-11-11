package edu.stonybrook.cse308.gerrybackend.graph.edges;

import edu.stonybrook.cse308.gerrybackend.data.Joinability;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class GerryEdge<N extends GerryNode> extends UnorderedPair<N> {

    @Getter
    @Id
    @Column(name="id")
    protected String id;

    @Embedded
    private Joinability joinability;

    protected GerryEdge(UUID id, N node1, N node2){
        this.id = id.toString();
        this.add(node1);
        this.add(node2);
        this.joinability = new Joinability(this.item1, this.item2);
    }

    public double getJoinabilityValue(Set<PoliticalParty> partyTypes, Set<DemographicType> demoTypes){
        return this.joinability.getValue(partyTypes, demoTypes);
    }
}
