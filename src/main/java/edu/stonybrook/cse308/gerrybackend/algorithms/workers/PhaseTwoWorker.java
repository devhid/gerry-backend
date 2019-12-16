package edu.stonybrook.cse308.gerrybackend.algorithms.workers;

import edu.stonybrook.cse308.gerrybackend.algorithms.heuristics.phasetwo.PhaseTwoPrecinctMoveHeuristic;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.measures.InputMeasures;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.data.algorithm.PrecinctMove;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseTwoMoveDelta;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoDepth;
import edu.stonybrook.cse308.gerrybackend.enums.heuristics.PhaseTwoPrecinctMove;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.initializers.PhaseTwoReportInitializer;

import java.util.*;

public class PhaseTwoWorker extends AlgPhaseWorker<PhaseTwoInputs, PhaseTwoReport> {

    /**
     * Determines whether or not the worker should finish the simulated annealing.
     *
     * @param inputs          phase two inputs
     * @param potentialMoves map whose key is a potential move and value is the resulting StateNode graph
     * @return a boolean value describing whether the worker should stop
     */
    private static boolean shouldStop(PhaseTwoInputs inputs, Map<PrecinctMove, StateNode> potentialMoves) {
        // current OF
        double currentScore = computeObjectiveFunction(inputs);

        // compute OF for each move
        for(StateNode state: potentialMoves.values()) {
            double newScore = computeObjectiveFunction(inputs);

            // if we find a better OF, don't stop
            if (currentScore < newScore) {
                return false;
            }
        }

        // check if we want to compute more potential moves
        if(inputs.getNumRetries() != 0) {
            // decrement number of retries
            inputs.setNumRetries(inputs.getNumRetries() - 1);
            return false;
        }

        return true;
    }

    /**
     * Computes the potential PrecinctMoves on a given StateNode graph according to the heuristics specified.
     *
     * @param state          the current StateNode graph
     * @param inputs        the phase two inputs
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Map<PrecinctMove, StateNode> computePotentialMoves(StateNode state, PhaseTwoInputs inputs) {
        Map<PrecinctMove, StateNode> potentialMoves = null;
        PhaseTwoDepth depthHeuristic = inputs.getDepthHeuristic();

        if (depthHeuristic == PhaseTwoDepth.STANDARD) {
            potentialMoves = computePotentialMovesStandard(state, inputs);
        } else if (depthHeuristic == PhaseTwoDepth.LEVEL) {
//            potentialMoves = computePotentialMovesLevel(state, moveHeuristic);
        } else if (depthHeuristic == PhaseTwoDepth.TREE) {
//            potentialMoves = computePotentialMovesTree(state, moveHeuristic);
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
    private static Map<PrecinctMove, StateNode> computePotentialMovesStandard(StateNode state,
                                                                              PhaseTwoInputs inputs) {
        Map<PrecinctMove, StateNode> potentialMoves = new HashMap<>();
        PrecinctMove move = PhaseTwoPrecinctMoveHeuristic.selectPrecinct(inputs.getMoveHeuristic(), state, inputs);

        StateNode result = null;
        try {
            result = state.copyAndExecuteMove(move);
        } catch (MismatchedElectionException e) {
            e.printStackTrace();
        }

        potentialMoves.put(move, result);
        return potentialMoves;
    }

    /**
     * Computes the potential PrecinctMoves on a given StateNode graph by considering n different PrecinctMoves.
     *
     * @param state         the current StateNode graph
     * @param moveHeuristic the manner in which a precinct shall be selected to be part of a move
     * @return map whose key is a potential move and value is the resulting StateNode graph
     */
    private static Map<PrecinctMove, StateNode> computePotentialMovesLevel(StateNode state, PhaseTwoPrecinctMove moveHeuristic) {
        // TODO: fill in
        Map<PrecinctMove, StateNode> potentialMoves = new HashMap<>();
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
    private static Map<PrecinctMove, StateNode> computePotentialMovesTree(StateNode state, PhaseTwoPrecinctMove moveHeuristic) {
        // TODO: fill in
        Map<PrecinctMove, StateNode> potentialMoves = new HashMap<>();
        return potentialMoves;
    }

    /**
     * Computes the objective function score for a given StateNode graph.
     *
     * @param inputs the properties involved in computing the objective function
     * @return a value in the range [0,1]
     */
    private static double computeObjectiveFunction(PhaseTwoInputs inputs) {
         return inputs.getState().getChildren()
                .stream()
                .flatMap(district -> inputs.getWeightMap().keySet()
                        .stream()
                        .map(measure -> InputMeasures.computeScore(measure, district) * inputs.getWeightMap().get(measure)))
                .reduce(0.0, Double::sum);
    }

    /**
     * Selects the best PrecinctMove (one with the highest objective function score) out of all the potential moves.
     *
     * @param potentialMoves map whose key is a potential move and value is the resulting StateNode graph
     * @return the best PrecinctMove (one with the highest objective function score)
     */
    private static PrecinctMove selectPrecinctMove(PhaseTwoInputs inputs, Map<PrecinctMove, StateNode> potentialMoves) {
        PrecinctMove bestMove = null;
        double bestMoveScore = 0.0;

        for (Map.Entry<PrecinctMove, StateNode> entry : potentialMoves.entrySet()) {
            double potentialMoveScore = computeObjectiveFunction(inputs);
            if (bestMove == null || potentialMoveScore > bestMoveScore) {
                bestMove = entry.getKey();
                bestMoveScore = potentialMoveScore;
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

    public PhaseTwoReport run(PhaseTwoInputs inputs) {
        int iteration = inputs.getJob().getNextPhaseTwoIteration();
        final Queue<PhaseTwoMoveDelta> deltas = new LinkedList<>();
        final StateNode state = inputs.getState();

        Map<PrecinctMove, StateNode> potentialMoves = computePotentialMoves(state, inputs);
        while (!shouldStop(inputs, potentialMoves)) {
            PrecinctMove move = selectPrecinctMove(inputs, potentialMoves);
            PhaseTwoMoveDelta iterationDelta = executePrecinctMove(state, move, iteration);
            deltas.offer(iterationDelta);
            iteration = inputs.getJob().getNextPhaseTwoIteration();
            potentialMoves = computePotentialMoves(state, inputs);
        }
        return PhaseTwoReportInitializer.initClass(null, deltas);
    }

}
