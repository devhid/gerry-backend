package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MapUtils {

    public static void initMap(Map map, Object defaultValue) {
        for (Object enumVal : map.keySet()) {
            map.put(enumVal, defaultValue);
        }
    }

    public static Map<String, String> transformInitialPrecinctAssignments(Map<PrecinctNode, DistrictNode> map) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getName(), e -> e.getValue().getNumericalId()));
    }

    public static Map<String, String> transformMapEntriesToNumericalIds(Map<DistrictNode, DistrictNode> map) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getNumericalId(), e -> e.getValue().getNumericalId()));
    }

    public static Map<String, String> transformMapEntriesToIds(Map<? extends GerryNode, ? extends GerryNode> map) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getId(), e -> e.getValue().getId()));
    }

    public static int sumIntValuedMap(Map<?, Integer> map) {
        Collection<Integer> allValues = map.values();
        return allValues.stream().mapToInt(Integer::intValue).sum();
    }

}
