package edu.stonybrook.cse308.gerrybackend.graph;

import edu.stonybrook.cse308.gerrybackend.data.Joinability;
import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
public class GerryEdge {

    @Id
    @Column(name="id")
    private String id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name="item1", column = @Column( name="node_item_one" )),
            @AttributeOverride( name="item2", column = @Column( name="node_item_two" )),
    })
    @NotNull
    @Getter
    private UnorderedPair<GerryGeoNode> nodes;

    private Joinability joinability;

    public GerryEdge(UUID id, GerryGeoNode node1, GerryGeoNode node2){
        this.id = id.toString();
        this.nodes = new UnorderedPair<>(node1, node2);
        this.joinability = new Joinability(node1, node2);
    }

    public boolean contains(GerryGeoNode node){
        return nodes.contains(node);
    }

    public double getJoinabilityValue(Set<DemographicType> demoTypes){
        return this.joinability.getValue(demoTypes);
    }
}
