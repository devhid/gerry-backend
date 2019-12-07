package edu.stonybrook.cse308.gerrybackend.graph.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.enums.converters.NodeTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import lombok.Getter;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MappedSuperclass
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DistrictNode.class, name = "district"),
        @JsonSubTypes.Type(value = StateNode.class, name = "state")
})
public abstract class ClusterNode<E extends GerryEdge, C extends GerryNode> extends GerryNode<E, StateNode> {

    @Getter
    @ManyToMany(cascade = CascadeType.ALL)
    protected Set<C> children;

    @Getter
    @ElementCollection
    protected Set<String> counties;

    @Getter
    @Convert(converter = NodeTypeConverter.class)
    protected NodeType nodeType;

    @Transient
    @JsonIgnore
    protected MultiPolygon multiPolygon;

    @Transient
    @JsonIgnore
    protected Geometry boundingCircle;

    @Transient
    @JsonIgnore
    protected Geometry convexHull;

    protected ClusterNode() {
        super();
        this.children = null;
        this.counties = new HashSet<>();
        this.nodeType = NodeType.NOT_SET;
    }

    protected ClusterNode(String id, String name, NodeType nodeType, DemographicData demographicData,
                          ElectionData electionData, Set<E> adjacentEdges, String geography,
                          Set<C> children, Set<String> counties, StateNode parent) {
        super(id, name, demographicData, electionData, adjacentEdges, geography);
        this.nodeType = nodeType;
        this.setChildren(children);
        if (counties == null) {
            this.loadAllCounties();
        } else {
            this.counties = counties;
        }
        this.parent = parent;
    }

    int size() {
        return this.children.size();
    }

    protected abstract void loadAllCounties();

    protected void setChildren(Set<C> children) {
        this.children = children;
        for (C child : children) {
            // TODO: how to suppress this?
            child.setParent(this);
        }
        this.loadAllCounties();
        this.aggregateStatistics();
    }

    public Set<C> clearAndReturnChildren() {
        Set<C> children = this.children;
        this.children = null;
        return children;
    }

    public void aggregateStatistics() {
        ElectionData aggregateElections = null;
        DemographicData aggregateDemographics = null;
        for (C child : this.children) {
            if (child instanceof ClusterNode) {
                if (child.getElectionData() == null || child.getDemographicData() == null) {
                    ((ClusterNode) child).aggregateStatistics();
                }
            }
            if (aggregateElections == null || aggregateDemographics == null) {
                aggregateElections = new ElectionData(child.getElectionData());
                aggregateDemographics = new DemographicData(child.getDemographicData());
            } else {
                try {
                    aggregateElections = ElectionData.combine(aggregateElections, child.getElectionData());
                    aggregateDemographics = DemographicData.combine(aggregateDemographics, child.getDemographicData());
                } catch (MismatchedElectionException e) {
                    // should never happen
                    e.printStackTrace();
                }
            }
        }
        this.electionData = aggregateElections;
        this.demographicData = aggregateDemographics;
    }

    @Override
    public Geometry getGeometry() {
        return this.getMultiPolygon();
    }

    protected void computeMultiPolygon() throws ParseException {
        List<Polygon> polygons = new ArrayList<>();
        for (C child : this.children) {
            Geometry childGeometry = child.getGeometry();
            if (childGeometry instanceof Polygon) {
                polygons.add((Polygon) childGeometry);
            } else {
                polygons.add((Polygon) childGeometry.convexHull());
            }
        }
        Polygon[] polygonsArr = new Polygon[polygons.size()];
        polygonsArr = polygons.toArray(polygonsArr);
        this.multiPolygon = new MultiPolygon(polygonsArr, new GeometryFactory());
    }

    protected void computeConvexHull() throws ParseException {
        if (this.multiPolygon == null) {
            this.computeMultiPolygon();
        }
        this.convexHull = this.multiPolygon.convexHull();
    }

    protected void computeBoundingCircle() throws ParseException {
        if (this.multiPolygon == null) {
            this.computeMultiPolygon();
        }
        this.boundingCircle = new MinimumBoundingCircle(this.multiPolygon).getCircle();
    }

    public MultiPolygon getMultiPolygon() {
        try {
            if (this.multiPolygon == null) {
                this.computeMultiPolygon();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this.multiPolygon;
    }

    public Geometry getConvexHull() {
        try {
            if (this.convexHull == null) {
                this.computeConvexHull();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this.convexHull;
    }

    public Geometry getBoundingCircle() {
        try {
            if (this.boundingCircle == null) {
                this.computeBoundingCircle();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this.boundingCircle;
    }

    protected void markGeometriesStale() {
        this.multiPolygon = null;
        this.boundingCircle = null;
        this.convexHull = null;
    }

    protected void updateGeometries() throws ParseException {
        this.computeMultiPolygon();
        this.computeBoundingCircle();
        this.computeConvexHull();
    }

}
