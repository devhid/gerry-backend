package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo.PhaseTwoPrecinctMoveHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.InputMeasures;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureInterface;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.initializers.PhaseTwoReportInitializer;

import java.util.*;

public class PhaseTwoWorker extends AlgPhaseWorker<PhaseTwoInputs, PhaseTwoReport> {

    /**
     * Determines whether or not any potential move is better than the current districting.
     *
     * @param inputs          phase two inputs
     * @param potentialMoves map whose key is a potential move and value is the resulting StateNode graph
     * @return a boolean value describing whether the worker should stop
     */
    private static boolean shouldStop(PhaseTwoInputs inputs, Set<PrecinctMove> potentialMoves,
                                      Map<DistrictNode, Double> computedScores) {
        if (potentialMoves.size() == 0) {
            return true;
        }

        Map<MeasureInterface, Double> weightMap = inputs.getWeightMap();
        boolean stop = true;

        for (PrecinctMove precinctMove : potentialMoves) {
            try {
                Map<DistrictNode, DistrictNode> changedDistricts = precinctMove.getNewDistricts();
                double currentScore = computeObjectiveFunction(changedDistricts.keySet(), weightMap, computedScores);
                double potentialScore = computeObjectiveFunction(changedDistricts.values(), weightMap, computedScores);
                if (currentScore + inputs.getEpsilon() < potentialScore) {
                    stop = false;
                    break;
                }
            } catch (MismatchedElectionException e) {
                e.printStackTrace();
            }
        }

        return stop;
    }

    /**
     * Computes the potential PrecinctMoves on a given StateNode graph according to the heuristics specified.
     *
     * @param state          the current StateNode graph
     * @param inputs        the phase two inputs
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Set<PrecinctMove> computePotentialMoves(StateNode state, PhaseTwoInputs inputs) {
        Set<PrecinctMove> potentialMoves;
        PhaseTwoDepth depthHeuristic = inputs.getDepthHeuristic();

        if (depthHeuristic == PhaseTwoDepth.STANDARD) {
            potentialMoves = computePotentialMovesStandard(state, inputs);
        } else if (depthHeuristic == PhaseTwoDepth.LEVEL) {
//            potentialMoves = computePotentialMovesLevel(state, moveHeuristic);
            throw new UnsupportedOperationException("Replace this string later!");
        } else if (depthHeuristic == PhaseTwoDepth.TREE) {
//            potentialMoves = computePotentialMovesTree(state, moveHeuristic);
            throw new UnsupportedOperationException("Replace this string later!");
        } else {
            throw new IllegalArgumentException("Replace this string later!");
        }
        return potentialMoves;
    }

    /**
     * Computes the potential PrecinctMove on a given StateNode graph in the standard manner.
     * This computes a single PrecinctMove.
     *
     * @param state         the current StateNode graph
     * @param inputs        the phase two inputs
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Set<PrecinctMove> computePotentialMovesStandard(StateNode state, PhaseTwoInputs inputs) {
        Set<PrecinctMove> potentialMoves = new HashSet<>();
        PrecinctMove move = PhaseTwoPrecinctMoveHeuristic.selectPrecinct(inputs);
        potentialMoves.add(move);
        return potentialMoves;
    }

    /**
     * Computes the potential PrecinctMoves on a given StateNode graph by considering n different PrecinctMoves.
     *
     * @param state         the current StateNode graph
     * @param moveHeuristic the manner in which a precinct shall be selected to be part of a move
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Set<PrecinctMove> computePotentialMovesLevel(StateNode state, PhaseTwoPrecinctMove moveHeuristic) {
        // TODO: fill in
        Set<PrecinctMove> potentialMoves = new HashSet<>();
        return potentialMoves;
    }

    /**
     * Computes the potential PrecinctMoves on a given StateNode graph by considering n different PrecinctMoves at
     * a depth of m levels (a perfect n-ary tree with a depth of m).
     * <p>
     * After the PrecinctMoves are calculated, for each of the n different initial PrecinctMoves, it selects the best
     * branch, where the best branch is a path from the initial PrecinctMove to the leaf node with the highest
     * objective function score. As such, it is optimistic in nature.
     *
     * @param state         the current StateNode graph
     * @param moveHeuristic the manner in which a precinct shall be selected to be part of a move
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Set<PrecinctMove> computePotentialMovesTree(StateNode state, PhaseTwoPrecinctMove moveHeuristic) {
        // TODO: fill in
        Set<PrecinctMove> potentialMoves = new HashSet<>();
        return potentialMoves;
    }

    private static double computeObjectiveFunction(DistrictNode district, Map<MeasureInterface, Double> weightMap,
                                                   Map<DistrictNode, Double> computedScores) {
        if (computedScores.containsKey(district)) {
            return computedScores.get(district);
        }
        double score = weightMap.keySet()
                .stream()
                .map(measure -> InputMeasures.computeScore(measure, district) * weightMap.get(measure))
                .reduce(0.0, Double::sum);
        computedScores.put(district, score);
        return score;
    }

    /**
     * Computes the objective function score for a collection of DistrictNodes.
     */
    private static double computeObjectiveFunction(Collection<DistrictNode> districts, Map<MeasureInterface, Double> weightMap,
                                                   Map<DistrictNode, Double> computedScores) {
        return districts.stream()
                .map(d -> computeObjectiveFunction(d, weightMap, computedScores))
                .reduce(0.0, Double::sum);
    }

    /**
     * Selects the best PrecinctMove (one with the highest objective function score) out of all the potential moves.
     *
     * @param potentialMoves
     * @return the best PrecinctMove (one with the highest objective function score)
     */
    private static PrecinctMove selectPrecinctMove(PhaseTwoInputs inputs, Set<PrecinctMove> potentialMoves,
                                                   Map<DistrictNode, Double> computedScores) {
        PrecinctMove bestMove = null;
        double bestMoveScore = Double.MIN_VALUE;
        Map<MeasureInterface, Double> weightMap = inputs.getWeightMap();
        for (PrecinctMove precinctMove : potentialMoves) {
            try {
                Map<DistrictNode, DistrictNode> changedDistricts = precinctMove.getNewDistricts();
                double potentialScore = computeObjectiveFunction(changedDistricts.values(), weightMap, computedScores);
                if (potentialScore > bestMoveScore + inputs.getEpsilon()) {
                    bestMove = precinctMove;
                    bestMoveScore = potentialScore;
                }
            } catch (MismatchedElectionException e) {
                e.printStackTrace();
            }
        }
        return bestMove;
    }

    /**
     * Executes a given PrecinctMove on the given StateNode graph.
     *
     * @param state     the given StateNode graph
     * @param move      the given PrecinctMove
     * @param iteration the current iteration
     * @return a PhaseTwoMoveDelta record of the executed PrecinctMove
     */
    private static PhaseTwoMoveDelta executePrecinctMove(StateNode state, PrecinctMove move, int iteration) {
        PhaseTwoMoveDelta delta = null;
        try {
            // Execute the PrecinctMove object.
            state.executeMove(move);    // throws MismatchedElectionException

            // Create a PhaseTwoMoveDelta record of the PrecinctMove.
            delta = PhaseTwoMoveDelta.fromPrecinctMove(move, iteration);
        } catch (MismatchedElectionException e) {
            // should never happen
            e.printStackTrace();
        }
        return delta;
    }

    @Override
    public PhaseTwoReport run(PhaseTwoInputs inputs) {
        final Queue<PhaseTwoMoveDelta> deltas = new LinkedList<>();
        final StateNode state = inputs.getState();
        int iteration = inputs.getJob().getNextPhaseTwoIteration();
        Set<PrecinctMove> potentialMoves = computePotentialMoves(state, inputs);
        Map<DistrictNode, Double> computedScores = new HashMap<>();
        boolean stop;
        while (inputs.getNumRetries() != 0) {
            stop = shouldStop(inputs, potentialMoves, computedScores);
            if (stop) {
                // check if we want to compute more potential moves
                if (inputs.getNumRetries() != 0) {
                    // decrement number of retries
                    inputs.setNumRetries(inputs.getNumRetries() - 1);
                }
                for (PrecinctMove potentialMove : potentialMoves) {
                    try {
                        Map<DistrictNode, DistrictNode> newDistricts = potentialMove.getNewDistricts();
                        for (DistrictNode newDistrict : newDistricts.values()) {
                            computedScores.remove(newDistrict);
                        }
                    } catch (MismatchedElectionException e) {
                        e.printStackTrace();
                    }
                }
                potentialMoves = computePotentialMoves(state, inputs);
                continue;
            }
            PrecinctMove move = selectPrecinctMove(inputs, potentialMoves, computedScores);
            PhaseTwoMoveDelta iterationDelta = executePrecinctMove(state, move, iteration);
            deltas.offer(iterationDelta);
            return PhaseTwoReportInitializer.initClass(StatusCode.IN_PROGRESS, inputs.getJobId(), deltas);
        }
        return PhaseTwoReportInitializer.initClass(StatusCode.SUCCESS, inputs.getJobId(), deltas);
    }

}
