package edu.stonybrook.cse308.gerrybackend.communication;

import com.fasterxml.jackson.annotation.JsonRawValue;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

public class GeoPrecinct {

    private String id;

    @JsonRawValue
    private String geometry;

    public GeoPrecinct(String id, String geometry) {
        this.id = id;
        this.geometry = geometry;
    }

    public static GeoPrecinct fromPrecinctNode(PrecinctNode precinct) {
        return new GeoPrecinct(precinct.getId(), precinct.getGeometryJson());
    }

}
