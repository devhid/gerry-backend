package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;

import java.util.*;
import java.util.stream.Collectors;

public class GraphUtils {

    public static boolean isBridgePrecinct(DistrictNode district, PrecinctNode precinct) {
        Set<PrecinctNode> connectedPrecincts = new HashSet<>();
        Set<PrecinctNode> otherPrecincts = district.getChildren();
        otherPrecincts.remove(precinct);
        PrecinctNode currPrecinct = RandomUtils.getRandomElement(otherPrecincts);
        Queue<PrecinctNode> toVisit = new LinkedList<>();
        toVisit.offer(currPrecinct);
        connectedPrecincts.add(currPrecinct);
        while (!toVisit.isEmpty()) {
            currPrecinct = toVisit.poll();
            Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(currPrecinct.getAdjacentNodes(), PrecinctNode.class);
            adjPrecincts = adjPrecincts.stream().filter(p -> p.getParent() == district).collect(Collectors.toSet());
            for (PrecinctNode p : adjPrecincts) {
                if (p != precinct && !connectedPrecincts.contains(p)) {
                    toVisit.offer(p);
                    connectedPrecincts.add(p);
                }
            }
        }
        return connectedPrecincts.size() != otherPrecincts.size();
    }
}
