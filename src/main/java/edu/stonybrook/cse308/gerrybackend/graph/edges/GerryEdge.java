package edu.stonybrook.cse308.gerrybackend.graph.edges;

import com.fasterxml.jackson.annotation.*;
import edu.stonybrook.cse308.gerrybackend.data.Joinability;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@MappedSuperclass
@JsonTypeInfo(
        use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value=PrecinctEdge.class, name="precinct_edge"),
        @JsonSubTypes.Type(value=DistrictEdge.class, name="district_edge")
})
@JsonIdentityInfo(
        generator=ObjectIdGenerators.PropertyGenerator.class,
        property="id"
)
public abstract class GerryEdge<N extends GerryNode> extends UnorderedPair<N> {

    @Getter
    @Id
    @Column(name="id")
    protected String id;

    @Embedded
    @JsonIgnore
    private Joinability joinability;

    protected GerryEdge(String id, N node1, N node2){
        this.id = id;
        this.add(node1);
        this.add(node2);
        this.joinability = new Joinability(node1, node2);
    }

    public double getJoinabilityValue(Set<PoliticalParty> partyTypes, Set<DemographicType> demoTypes){
        return this.joinability.getValue(partyTypes, demoTypes);
    }
}
