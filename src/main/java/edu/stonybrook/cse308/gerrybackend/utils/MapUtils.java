package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.data.UnorderedPair;
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

    public static void populateEdgeNodeReferences(Map<GerryEdge, UnorderedPair<GerryNode>> edgeMap, Set<? extends GerryNode> nodes){
        for (GerryNode node : nodes){
            Set<GerryEdge> nodeEdges = node.getAdjacentEdges();
            for (GerryEdge nodeEdge : nodeEdges){
                if (!edgeMap.containsKey(nodeEdge)){
                    UnorderedPair<GerryNode> edgeNodes = new UnorderedPair<>();
                    edgeNodes.add(node);
                    edgeMap.put(nodeEdge, edgeNodes);
                }
                else {
                    edgeMap.get(nodeEdge).add(node);
                }
            }
        }
    }
}
