package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.Heuristics;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone.MergedDistrict;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.CandidatePairs;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.LikelyCandidatePair;
import edu.stonybrook.cse308.gerrybackend.data.comparators.SmallestLikelyPairsComparator;
import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedStringPair;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseOneStop;
import edu.stonybrook.cse308.gerrybackend.enums.types.*;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.initializers.PhaseOneReportInitializer;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import edu.stonybrook.cse308.gerrybackend.utils.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PhaseOneWorker extends AlgPhaseWorker<PhaseOneInputs, PhaseOneReport> {

    private int initialIteration;

    public PhaseOneWorker(int initialIteration) {
        this.initialIteration = initialIteration;
    }

    /**
     * Creates an initial district for each precinct and populates an adjacency list for the newly created districts.
     * @param allPrecincts              the input StateNode
     * @param precinctToDistrict map whose key is the precinct and the value is the initial containing district
     * @param newDistrictAdjList map whose key is a district and value is the set of adjacent districts
     */
    private void createInitialDistricts(Set<PrecinctNode> allPrecincts,
                                        Map<PrecinctNode, DistrictNode> precinctToDistrict,
                                        Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList) {
        int nextNumericalId = 1;
        for (PrecinctNode p : allPrecincts) {
//            if (p.getId().equals("4b738f1d-b4cb-4632-b823-9bec59beaf91")) {
//                System.out.println("its the precinct");
//            }
            if (!precinctToDistrict.containsKey(p)) {
                DistrictNode newDistrict = DistrictNode.childBuilder()
                        .child(p)
                        .nodeType(NodeType.USER)
                        .build();
                newDistrict.setNew(true);
                newDistrict.setNumericalId(Integer.toString(nextNumericalId++));
                precinctToDistrict.put(p, newDistrict);
            }
            final DistrictNode district = precinctToDistrict.get(p);
            final Set<PrecinctNode> adjPrecincts = GenericUtils.castSetOfObjects(p.getAdjacentNodes(), PrecinctNode.class);
            if (!newDistrictAdjList.containsKey(district)) {
                newDistrictAdjList.put(district, new HashSet<>());
            }
            final Set<DistrictNode> adjacentNodes = newDistrictAdjList.get(district);
            for (PrecinctNode adjP : adjPrecincts) {
                if (!precinctToDistrict.containsKey(adjP)) {
                    DistrictNode newAdjDistrict = DistrictNode.childBuilder()
                            .child(adjP)
                            .nodeType(NodeType.USER)
                            .build();
                    newAdjDistrict.setNew(true);
                    newAdjDistrict.setNumericalId(Integer.toString(nextNumericalId++));
                    precinctToDistrict.put(adjP, newAdjDistrict);
                }
                DistrictNode adjDistrict = precinctToDistrict.get(adjP);
                adjacentNodes.add(adjDistrict);
            }
        }
    }

    /**
     * Creates DistrictEdge references amongst the newly created districts.
     *
     * @param newDistrictAdjList map whose key is a district and value is the set of adjacent districts
     */
    private void createInitialEdges(Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList) {
        final Set<UnorderedStringPair> createdEdges = new HashSet<>();
        newDistrictAdjList.forEach((district, adjDistricts) -> {
            adjDistricts.forEach(adjDistrict -> {
                UnorderedStringPair districtIdPair = new UnorderedStringPair(district.getId(), adjDistrict.getId());
                if (createdEdges.contains(districtIdPair)) {
                    return;
                }
                createdEdges.add(districtIdPair);
                DistrictEdge edge = new DistrictEdge(UUID.randomUUID().toString(), district, adjDistrict);
                edge.setNew(true);
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
     *
     * @param precinctToDistrict map whose key is the precinct and the value is the initial containing district
     * @param demoTypes          the user input selected demographic types
     * @return PhaseOneMergeDelta describing the assignment
     */
    private PhaseOneMergeDelta createInitialDelta(Map<PrecinctNode, DistrictNode> precinctToDistrict,
                                                  Set<DemographicType> demoTypes) {
        Map<String, String> initialPrecinctAssignment = MapUtils.transformInitialPrecinctAssignments(precinctToDistrict);
        Map<String, MergedDistrict> newDistricts = precinctToDistrict.values().stream()
                .collect(Collectors.toMap(DistrictNode::getNumericalId, d -> MergedDistrict.fromDistrictNode(d, demoTypes)));
        return new PhaseOneMergeDelta(this.initialIteration, initialPrecinctAssignment, newDistricts);
    }

    /**
     * Assigns each precinct in the state its own initial district.
     *
     * @param inputs    the inputs for phase 1
     * @return a delta describing the assignment
     */
    private PhaseOneMergeDelta assignInitialDistricts(PhaseOneInputs inputs) {
        final StateNode originalState = inputs.getState();
        final Map<PrecinctNode, DistrictNode> precinctToDistrict = new HashMap<>();
        final Map<DistrictNode, Set<DistrictNode>> newDistrictAdjList = new HashMap<>();
        final Set<PrecinctNode> allPrecincts = originalState.getAllPrecincts();

        originalState.fillInTransientProperties();
        createInitialDistricts(allPrecincts, precinctToDistrict, newDistrictAdjList);
        createInitialEdges(newDistrictAdjList);

        final StateNode newState = StateNode.builder()
                .id(UUID.randomUUID().toString())
                .nodeType(NodeType.USER)
                .stateType(inputs.getStateType())
                .districts(new HashSet<>(precinctToDistrict.values()))
                .redistrictingLegislation(originalState.getRedistrictingLegislation())
                .build();
        newState.setNew(true);
        inputs.setState(newState);

        Job job = new Job(AlgPhaseType.PHASE_ONE, newState);
        inputs.setJobId(job.getId());
        inputs.setJob(job);

        return createInitialDelta(precinctToDistrict, inputs.getDemographicTypes());
    }

    /**
     * Determines the majority-minority pairs for the given state.
     * The strategy used depends on the heuristic specified.
     *
     * @param inputs the inputs for phase 1
     * @return a set of LikelyCandidatePair objects representing which districts should be merged
     */
    private static Set<LikelyCandidatePair> determineMajorityMinorityPairs(PhaseOneInputs inputs) {
        return Heuristics.determineMajMinPairs(inputs);
    }

    /**
     * Determines the non-majority-minority pairs for the given state.
     * The strategy used depends on the heuristic specified.
     *
     * @param inputs      the inputs for phase 1
     * @param majMinPairs the set of previously identified majority-minority pairs
     * @return a set of LikelyCandidatePair objects representing which districts should be merged
     */
    private static Set<LikelyCandidatePair> determineOtherPairs(PhaseOneInputs inputs, Set<LikelyCandidatePair> majMinPairs) {
        return Heuristics.determineOtherPairs(inputs, majMinPairs);
    }

    /**
     * Determines the majority-minority and non-MM candidate pairs for a given StateNode graph using specified heuristics.
     *
     * @param inputs the inputs for phase 1
     * @return a CandidatePairs object containing the majority-minority and non-MM candidate pairs
     */
    private static CandidatePairs determineCandidatePairs(PhaseOneInputs inputs) {
        Set<LikelyCandidatePair> majMinPairs = determineMajorityMinorityPairs(inputs);
        Set<LikelyCandidatePair> otherPairs = determineOtherPairs(inputs, majMinPairs);
        return new CandidatePairs(majMinPairs, otherPairs);
    }

    /**
     * Determines whether or not the given algorithm run is in its last iteration by comparing the number of districts.
     *
     * @param state        the StateNode graph
     * @param pairs        the majority-minority and non-MM pairs identified for this iteration
     * @param numDistricts the input number of desired districts
     * @return whether or not the number of districts will have been reached after executing the determined pairs
     */
    private static boolean isLastIteration(StateNode state, CandidatePairs pairs, int numDistricts) {
        return state.getChildren().size() - pairs.size() <= numDistricts;
    }

    /**
     * Filters the determined candidate pairs for a specified number of allowed merges.
     * Exclusively used during the last iteration of the algorithm run.
     *
     * @param heuristic        the heuristic for filtering last iteration pairs
     * @param pairs            the majority-minority and non-MM pairs identified for this iteration
     * @param numAllowedMerges the number of allowed merges (less than the number of identified pairs)
     */
    private static void filterLastIterationPairs(PhaseOneStop heuristic, CandidatePairs pairs, int numAllowedMerges) {
        Heuristics.filterLastIterationPairs(heuristic, pairs, numAllowedMerges);
    }

    private static void filterBigDistricts(PhaseOneInputs inputs, CandidatePairs pairs) {
        Set<LikelyCandidatePair> allPairs = pairs.getAllPairs();
        List<LikelyCandidatePair> removedPairs = new ArrayList<>();
        double idealPop = ((double) inputs.getState().getDemographicData().getTotalPopulation()) / inputs.getNumDistricts();
        for (LikelyCandidatePair pair : allPairs) {
            DistrictNode d1 = pair.getItem1();
            DistrictNode d2 = pair.getItem2();
            double d1TotalPop = d1.getDemographicData().getTotalPopulation();
            double d2TotalPop = d2.getDemographicData().getTotalPopulation();
            if (d1TotalPop + d2TotalPop > 1.15 * idealPop || d1TotalPop > idealPop || d2TotalPop > idealPop) {
//                removedPairs.add(pair);
                double popPercentDiff = MathUtils.calculatePercentDifference(d1TotalPop, d2TotalPop);
                if (popPercentDiff > 1.0) {
                    continue;
                }
                DistrictNode smallerDistrict = (d1TotalPop > d2TotalPop) ? d1 : d2;
                if (smallerDistrict.getAdjacentEdges().size() > 1) {
                    removedPairs.add(pair);
                }
            }
//            else if (d1TotalPop > 1.00 * idealPop || d2TotalPop > 1.00 * idealPop) {
//                if (smallerDistrict.getAdjacentEdges().size() > 1) {
//                    removedPairs.add(pair);
//                }
//            }
        }
        pairs.getMajorityMinorityPairs().removeAll(removedPairs);
        pairs.getOtherPairs().removeAll(removedPairs);
        if (pairs.size() == 0) {
            removedPairs.sort(new SmallestLikelyPairsComparator());
            pairs.getOtherPairs().addAll(removedPairs.subList(0, 1));
        }
    }

    /**
     * Joins the identified candidate pairs for this iteration and produces a delta describing the merges.
     *
     * @param state     the StateNode graph
     * @param pairs     the majority-minority and non-MM pairs identified for this iteration
     * @param iteration the current iteration of the algorithm
     * @param demoTypes the demographic types selected by the user
     * @return a PhaseOneMergeDelta object describing the merges that occurred
     */
    private static PhaseOneMergeDelta joinCandidatePairs(StateNode state, CandidatePairs pairs, int iteration,
                                                         Set<DemographicType> demoTypes,
                                                         Set<DistrictNode> remnantDistricts) {
        Map<DistrictNode, DistrictNode> mergedDistricts = new HashMap<>();
        for (LikelyCandidatePair pair : pairs.getAllPairs()) {
            try {
                DistrictNode d1 = pair.getItem1();
                DistrictNode d2 = pair.getItem2();
                DistrictNode newDistrict = DistrictNode.combine(d1, d2, true, remnantDistricts);
                if (newDistrict.getDemographicData().getTotalPopulation() == 0){
                    System.out.println("woah");
                }
                mergedDistricts.put(d1, newDistrict);
                mergedDistricts.put(d2, newDistrict);
            } catch (MismatchedElectionException e) {
                // should never happen
                e.printStackTrace();
            }
        }
        state.remapDistrictReferences(mergedDistricts);
        Map<String, String> mergedDistrictsById = MapUtils.transformMapEntriesToNumericalIds(mergedDistricts);
        Set<DistrictNode> newMergedDistricts = new HashSet<>(mergedDistricts.values());
        Map<String, MergedDistrict> newDistricts = newMergedDistricts.stream()
                .collect(Collectors.toMap(DistrictNode::getNumericalId, d -> MergedDistrict.fromDistrictNode(d, demoTypes)));
        return new PhaseOneMergeDelta(iteration, mergedDistrictsById, newDistricts);
    }

    @Override
    public PhaseOneReport run(PhaseOneInputs inputs) {
        final Set<DemographicType> demoTypes = inputs.getDemographicTypes();
        final int numDistricts = inputs.getNumDistricts();
        final Queue<PhaseOneMergeDelta> deltas = new LinkedList<>();

        // Assign initial districts, modify inputs as appropriate, and produce the initial delta.
        if (inputs.getJob() == null) {
            PhaseOneMergeDelta initialDelta = assignInitialDistricts(inputs);
            deltas.offer(initialDelta);
            if (inputs.getAlgRunType() == AlgRunType.BY_STEP) {
                return PhaseOneReportInitializer.initClass(StatusCode.IN_PROGRESS, deltas, inputs.getJobId(), new HashSet<>());
            }
        }

        // Iteratively merge the districts.
        final StateNode state = inputs.getState();
        final Set<DistrictNode> remnantDistricts = new HashSet<>();
        StatusCode statusCode = StatusCode.IN_PROGRESS;
        int iteration;
        while (state.getChildren().size() != numDistricts) {
            iteration = inputs.getJob().getNextPhaseOneIteration();
            remnantDistricts.clear();
            CandidatePairs pairs = determineCandidatePairs(inputs);
            filterBigDistricts(inputs, pairs);
            boolean lastIteration = isLastIteration(state, pairs, numDistricts);
            if (lastIteration) {
                statusCode = StatusCode.SUCCESS;
                int numAllowedMerges = state.getChildren().size() - numDistricts;
                filterLastIterationPairs(inputs.getStopHeuristic(), pairs, numAllowedMerges);
            }
            PhaseOneMergeDelta iterationDelta = joinCandidatePairs(state, pairs, iteration, demoTypes, remnantDistricts);
            state.getChildren().removeAll(remnantDistricts);
            deltas.offer(iterationDelta);
            if (inputs.getAlgRunType() == AlgRunType.BY_STEP) {
                return PhaseOneReportInitializer.initClass(statusCode, deltas, inputs.getJobId(), remnantDistricts);
            }
        }
        return PhaseOneReportInitializer.initClass(statusCode, deltas, inputs.getJobId(), remnantDistricts);
    }
}
