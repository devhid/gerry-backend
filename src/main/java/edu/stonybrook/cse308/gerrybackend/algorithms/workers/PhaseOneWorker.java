package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.Heuristics;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.data.structures.UnorderedStringPair;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneMajMinPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneOtherPairs;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PhaseOneWorker extends AlgPhaseWorker<PhaseOneInputs, PhaseOneReport> {

    /**
     * Creates an initial district for each precinct and populates an adjacency list for the newly created districts.
     * @param allPrecincts all precincts for the given state
     * @param precinctToDistrict map whose key is the precinct and the value is the initial containing district
     * @param newDistrictAdjList map whose key is a district and value is the set of adjacent districts
     */
    private void createInitialDistricts(Set<PrecinctNode> allPrecincts,
                                        Map<PrecinctNode, DistrictNode> precinctToDistrict,
                                        Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList){
        allPrecincts.forEach(p -> {
            if (!precinctToDistrict.containsKey(p)){
                DistrictNode newDistrict = DistrictNode.childBuilder().child(p).build();
                precinctToDistrict.put(p, newDistrict);
            }
            final DistrictNode district = precinctToDistrict.get(p);
            final Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(p.getAdjacentNodes(), PrecinctNode.class);
            if (!newDistrictAdjList.containsKey(district)){
                newDistrictAdjList.put(district, new HashSet<>());
            }
            final Set<DistrictNode> adjacentNodes = newDistrictAdjList.get(district);
            adjPrecincts.forEach(adjP -> {
                if (!precinctToDistrict.containsKey(adjP)){
                    DistrictNode newAdjDistrict = DistrictNode.childBuilder().child(adjP).build();
                    precinctToDistrict.put(adjP, newAdjDistrict);
                }
                DistrictNode adjDistrict = precinctToDistrict.get(adjP);
                adjacentNodes.add(adjDistrict);
            });
        });
    }

    /**
     * Creates DistrictEdge references amongst the newly created districts.
     * @param newDistrictAdjList map whose key is a district and value is the set of adjacent districts
     */
    private void createInitialEdges(Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList){
        final Set<UnorderedStringPair> createdEdges = new HashSet<>();
        newDistrictAdjList.forEach((district, adjDistricts) -> {
            adjDistricts.forEach(adjDistrict -> {
                UnorderedStringPair districtIdPair = new UnorderedStringPair(district.getId(), adjDistrict.getId());
                if (createdEdges.contains(districtIdPair)){
                    return;
                }
                createdEdges.add(districtIdPair);
                DistrictEdge edge = new DistrictEdge(UUID.randomUUID().toString(), district, adjDistrict);
                try {
                    district.addEdge(edge);
                    adjDistrict.addEdge(edge);
                } catch (InvalidEdgeException e) {
                    // should never happen
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Creates the initial PhaseOneMergeDelta describing the assignment of each precinct to its own district.
     * Additionally, it adds the created delta to the end of the specified list.
     * @param precinctToDistrict map whose key is the precinct and the value is the initial containing district
     * @param deltas the queue containing the record of deltas per algorithm iteration
     * @param iteration the iteration number to assign to this initial delta
     */
    private void createInitialDelta(Map<PrecinctNode, DistrictNode> precinctToDistrict,
                                    Queue<PhaseOneMergeDelta> deltas, int iteration){
        Map<String, String> initialPrecinctAssignment = MapUtils.transformMapEntriesToIds(precinctToDistrict);
        Map<String, DistrictNode> newDistricts = precinctToDistrict.values().stream()
                .collect(Collectors.toMap(GerryNode::getId, Function.identity()));
        PhaseOneMergeDelta initialDelta = new PhaseOneMergeDelta(iteration, initialPrecinctAssignment, newDistricts);
        deltas.offer(initialDelta);
    }

    /**
     * Assigns each precinct in the state its own initial district.
     * @param state the original StateNode graph
     * @param deltas the queue containing the record of deltas per algorithm iteration
     * @param iteration the iteration number to assign to the initial delta produced
     * @return a user copy of a StateNode with the initial districts
     */
    private StateNode assignInitialDistricts(StateNode state, Queue<PhaseOneMergeDelta> deltas, int iteration){
        final Map<PrecinctNode, DistrictNode> precinctToDistrict = new HashMap<>();
        final Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList = new HashMap<>();
        final Set<PrecinctNode> allPrecincts = state.getAllPrecincts();

        createInitialDistricts(allPrecincts, precinctToDistrict, newDistrictAdjList);
        createInitialEdges(newDistrictAdjList);
        createInitialDelta(precinctToDistrict, deltas, iteration);

        return StateNode.builder()
                .nodeType(NodeType.USER)
                .stateType(state.getStateType())
                .districts(new HashSet<>(precinctToDistrict.values()))
                .build();
    }

    /**
     * Determines the majority-minority pairs for the given state.
     * The strategy used depends on the heuristic specified.
     * @param majMinPairsHeuristic the heuristic to determine majority-minority pairs
     * @param state the StateNode graph
     * @return a set of LikelyCandidatePair objects representing which districts should be merged
     */
    private Set<LikelyCandidatePair> determineMajorityMinorityPairs(PhaseOneMajMinPairs majMinPairsHeuristic,
                                                                    StateNode state) {
        return Heuristics.determineMajMinPairs(majMinPairsHeuristic, state);
    }

    /**
     * Determines the non-majority-minority pairs for the given state.
     * The strategy used depends on the heuristic specified.
     * @param otherPairsHeuristic the heuristic to determine non-majority-minority pairs
     * @param state the StateNode graph
     * @param majMinPairs the set of previously identified majority-minority pairs
     * @return a set of LikelyCandidatePair objects representing which districts should be merged
     */
    private Set<LikelyCandidatePair> determineOtherPairs(PhaseOneOtherPairs otherPairsHeuristic, StateNode state,
                                                         Set<LikelyCandidatePair> majMinPairs) {
        return Heuristics.determineOtherPairs(otherPairsHeuristic, state, majMinPairs);
    }

    /**
     * Determines the majority-minority and non-MM candidate pairs for a given StateNode graph using specified heuristics.
     * @param majMinPairsHeuristic the heuristic to determine majority-minority pairs
     * @param otherPairsHeuristic the heuristic to determine non-majority-minority pairs
     * @param state the StateNode graph
     * @return a CandidatePairs object containing the majority-minority and non-MM candidate pairs
     */
    private CandidatePairs determineCandidatePairs(PhaseOneMajMinPairs majMinPairsHeuristic,
                                                   PhaseOneOtherPairs otherPairsHeuristic, StateNode state) {
        Set<LikelyCandidatePair> majMinPairs = this.determineMajorityMinorityPairs(majMinPairsHeuristic, state);
        Set<LikelyCandidatePair> otherPairs = this.determineOtherPairs(otherPairsHeuristic, state, majMinPairs);
        return new CandidatePairs(majMinPairs, otherPairs);
    }

    /**
     * Determines whether or not the given algorithm run is in its last iteration by comparing the number of districts.
     * @param state the StateNode graph
     * @param pairs the majority-minority and non-MM pairs identified for this iteration
     * @param numDistricts the input number of desired districts
     * @return whether or not the number of districts will have been reached after executing the determined pairs
     */
    private boolean isLastIteration(StateNode state, CandidatePairs pairs, int numDistricts) {
        return state.getChildren().size() - pairs.size() <= numDistricts;
    }

    /**
     * Filters the determined candidate pairs for a specified number of allowed merges.
     * Exclusively used during the last iteration of the algorithm run.
     * @param heuristic the heuristic for filtering last iteration pairs
     * @param pairs the majority-minority and non-MM pairs identified for this iteration
     * @param numAllowedMerges the number of allowed merges (less than the number of identified pairs)
     */
    private void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges) {
        Heuristics.filterLastIterationPairs(heuristic, pairs, numAllowedMerges);
    }

    /**
     * Joins the identified candidate pairs for this iteration and produces a delta describing the merges.
     * @param state the StateNode graph
     * @param pairs the majority-minority and non-MM pairs identified for this iteration
     * @param iteration the current iteration of the algorithm
     * @return a PhaseOneMergeDelta object describing the merges that occurred
     */
    private PhaseOneMergeDelta joinCandidatePairs(StateNode state, CandidatePairs pairs, int iteration) {
        Map<DistrictNode, DistrictNode> mergedDistricts = new HashMap<>();
        for (LikelyCandidatePair pair : pairs.getAllPairs()) {
            try {
                DistrictNode d1 = pair.getItem1();
                DistrictNode d2 = pair.getItem2();
                DistrictNode newDistrict = DistrictNode.combine(d1, d2);
                mergedDistricts.put(d1, newDistrict);
                mergedDistricts.put(d2, newDistrict);
            } catch (MismatchedElectionException e) {
                // should never happen
                e.printStackTrace();
            }
        }
        state.remapDistrictReferences(mergedDistricts);
        Map<String, String> mergedDistrictsById = MapUtils.transformMapEntriesToIds(mergedDistricts);
        Map<String, DistrictNode> newDistricts = mergedDistricts.values().stream()
                .collect(Collectors.toMap(GerryNode::getId, Function.identity()));
        return new PhaseOneMergeDelta(iteration, mergedDistrictsById, newDistricts);
    }

    @Override
    public PhaseOneReport run(PhaseOneInputs inputs) {
        int iteration = 0;
        final Queue<PhaseOneMergeDelta> deltas = new LinkedList<>();
        final StateNode state = this.assignInitialDistricts(inputs.getState(), deltas, iteration++);

        final int numDistricts = inputs.getNumDistricts();
        while (state.getChildren().size() != numDistricts) {
            CandidatePairs pairs = this.determineCandidatePairs(inputs.getPhaseOneMajMinPairsHeuristic(),
                    inputs.getPhaseOneOtherPairsHeuristic(), inputs.getState());
            boolean lastIteration = this.isLastIteration(state, pairs, numDistricts);
            if (lastIteration) {
                int numAllowedMerges = state.getChildren().size() - numDistricts;
                this.filterLastIterationPairs(inputs.getStopHeuristic(), pairs, numAllowedMerges);
            }
            PhaseOneMergeDelta iterationDelta = this.joinCandidatePairs(state, pairs, iteration);
            deltas.offer(iterationDelta);
            iteration++;
        }
        return new PhaseOneReport(deltas);
    }
}
