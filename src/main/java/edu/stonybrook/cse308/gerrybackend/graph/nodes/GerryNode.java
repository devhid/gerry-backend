package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
@JsonIgnoreProperties({"adjacentNodes", "electionType", "populationDensity"})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PrecinctNode.class, name = "precinct"),
        @JsonSubTypes.Type(value = ClusterNode.class, name = "cluster")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public abstract class GerryNode<E extends GerryEdge, P extends ClusterNode> {

    @Getter
    @Id
    @Column(name = "id")
    protected String id;

    @Getter
    @Column(name = "name")
    protected String name;

    @Getter
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "node_demo_id")
    protected DemographicData demographicData;

    @Getter
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "node_election_id")
    protected ElectionData electionData;

    @Getter
    @ManyToMany(cascade = CascadeType.ALL)     // one node has many edges, an edge has 2 (many) nodes
    protected Set<E> adjacentEdges;

    @Lob
    @Column(name = "geojson", columnDefinition = "LONGTEXT")
    @JsonProperty(value = "geojson", defaultValue = "{}")
    protected String geoJson;

    @Getter
    @Setter
    @Transient
    protected P parent;

    @Getter
    @Transient
    @JsonIgnore
    private Geometry geometry;

    protected GerryNode() {
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.demographicData = new DemographicData();
        this.electionData = new ElectionData();
        this.adjacentEdges = new HashSet<>();
        this.geoJson = null;
    }

    protected GerryNode(String id, String name,
                        DemographicData demographicData, ElectionData electionData,
                        Set<E> adjacentEdges, String geoJson) {
        this.id = id;
        this.name = name;
        this.demographicData = demographicData;
        this.electionData = electionData;
        this.adjacentEdges = adjacentEdges;
        this.geoJson = geoJson;
    }

    public Set<GerryNode> getAdjacentNodes() {
        Set<GerryNode> adjNodes = new HashSet<>();
        for (E edge : adjacentEdges) {
            GerryNode adjNode = (GerryNode) ((edge.getItem1() == this) ? edge.getItem2() : edge.getItem1());
            adjNodes.add(adjNode);
        }
        return adjNodes;
    }

    public void addEdge(E edge) throws InvalidEdgeException {
        if (this.adjacentEdges.contains(edge)) {
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.add(edge);
    }

    public void removeEdge(E edge) throws InvalidEdgeException {
        if (!(this.adjacentEdges.contains(edge))) {
            throw new InvalidEdgeException("Replace this string later!");
        }
        this.adjacentEdges.remove(edge);
    }

    public void clearEdges() {
        this.adjacentEdges.clear();
    }

    public ElectionType getElectionType() {
        return this.electionData.getElectionType();
    }

    @JsonRawValue
    public String getGeoJson() {
        return this.geoJson;
    }

    public void setGeoJson(JsonNode node) {
        this.geoJson = node.toString();
    }

    public Geometry getGeometry() throws ParseException {
        if (this.geometry == null) {
            GeoJsonReader reader = new GeoJsonReader();
            this.geometry = reader.read(this.geoJson);
        }
        return this.geometry;
    }

    public double getPopulationDensity() {
        if (this.geometry != null && this.geometry.getArea() != 0) {
            return this.demographicData.getTotalPopulation() / this.geometry.getArea();
        }
        return -1.0;
    }

}
