package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.data.structures.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.graph.edges.GerryEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;

import java.util.Map;
import java.util.Set;

public class MapUtils {

    public static void initMap(Map map, Object defaultValue){
        for (Object enumVal : map.keySet()){
            map.put(enumVal, defaultValue);
        }
    }

    public static void populateEdgeNodeReferences(Map<String, UnorderedPair<GerryNode>> edgeMap, Set<? extends GerryNode> nodes){
        for (GerryNode node : nodes){
            Set<GerryEdge> nodeEdges = node.getAdjacentEdges();
            for (GerryEdge nodeEdge : nodeEdges){
                if (!edgeMap.containsKey(nodeEdge.getId())){
                    UnorderedPair<GerryNode> edgeNodes = new UnorderedPair<>();
                    edgeNodes.add(node);
                    edgeMap.put(nodeEdge.getId(), edgeNodes);
                }
                else {
                    edgeMap.get(nodeEdge.getId()).add(node);
                }
            }
        }
    }
}
