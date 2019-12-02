package edu.stonybrook.cse308.gerrybackend.communication;

import com.fasterxml.jackson.annotation.JsonRawValue;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

public class GeoPrecinct {

    private String id;

    @JsonRawValue
    private String geoJson;

    public GeoPrecinct(String id, String geoJson) {
        this.id = id;
        this.geoJson = geoJson;
    }

    public static GeoPrecinct fromPrecinctNode(PrecinctNode precinct) {
        return new GeoPrecinct(precinct.getId(), precinct.getGeoJson());
    }

}
