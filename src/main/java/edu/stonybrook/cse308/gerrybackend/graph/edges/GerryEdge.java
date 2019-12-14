package edu.stonybrook.cse308.gerrybackend.graph.edges;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.stonybrook.cse308.gerrybackend.data.graph.Joinability;
import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class GerryEdge<N extends GerryNode> extends UnorderedPair<N> {

    @Getter
    @Id
    @Column(name = "id")
    @JsonValue
    protected String id;

    @Transient
    private Joinability joinability;

    @Value("${gerry.hashcode.multiplier}")
    @Transient
    private int hashCodeMultiplier;

    protected GerryEdge() {
        this.id = UUID.randomUUID().toString();
    }

    protected GerryEdge(String id) {
        this.id = id;
    }

    protected GerryEdge(String id, N node1, N node2) {
        this.id = id;
        this.add(node1);
        this.add(node2);
        this.computeNewJoinability();
    }

    public void computeNewJoinability() {
        this.joinability = new Joinability(this.item1, this.item2);
    }

    public double getJoinabilityValue(Set<PoliticalParty> parties, Set<DemographicType> demoTypes) {
        if (this.joinability == null) {
            this.computeNewJoinability();
        }
        return this.joinability.getValue(parties, demoTypes);
    }

    public double getJoinabilityValue(Set<PoliticalParty> parties) {
        if (this.joinability == null) {
            this.computeNewJoinability();
        }
        return this.joinability.getValueWithoutMinority(parties);
    }

    @Override
    public boolean add(N n) {
        boolean returnVal = super.add(n);
        if (!returnVal) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends N> c) {
        boolean returnVal = super.addAll(c);
        if (!returnVal) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GerryEdge)) {
            return false;
        }
        GerryEdge otherEdge = (GerryEdge) obj;
        if (otherEdge.size() != this.size()) {
            return false;
        }
        boolean item1Found = true;
        boolean item2Found = true;
        if (this.item1 != null) {
            item1Found = otherEdge.contains(this.item1);
        }
        if (this.item2 != null) {
            item2Found = otherEdge.contains(this.item2);
        }
        return item1Found && item2Found && this.id.equals(otherEdge.id);
    }

    @Override
    public int hashCode() {
        return hashCodeMultiplier * super.hashCode() + this.id.hashCode();
    }

}
