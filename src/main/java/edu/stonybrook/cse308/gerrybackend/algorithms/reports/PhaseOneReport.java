package edu.stonybrook.cse308.gerrybackend.algorithms.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stonybrook.cse308.gerrybackend.algorithms.logging.builders.PhaseOneLogBuilder;
import edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone.MergedDistrict;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.enums.types.StatusCode;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class PhaseOneReport extends IterativeAlgPhaseReport<PhaseOneMergeDelta, PhaseOneLogBuilder> {

    @Getter
    @Setter
    protected String jobId;

    @Getter
    @JsonIgnore
    private Set<DistrictNode> remnantDistricts;

    @Getter
    @JsonIgnore
    private Set<DistrictEdge> remnantEdges;

    public PhaseOneReport(StatusCode statusCode, Queue<PhaseOneMergeDelta> deltas, PhaseOneLogBuilder logBuilder,
                          String jobId, Set<DistrictNode> remnantDistricts) {
        super(statusCode, deltas, logBuilder);
        this.jobId = jobId;
        this.remnantDistricts = remnantDistricts;
    }

    @Override
    protected IterativeAlgPhaseReport createNextReportFromDeltas(Queue<PhaseOneMergeDelta> deltas) {
        return new PhaseOneReport(this.statusCode, deltas, this.logBuilder, this.jobId, new HashSet<>());
    }

    public PhaseOneReport fetchNextReport(int num) {
        IterativeAlgPhaseReport nextReport = super.fetchNextReport(num);
        return (PhaseOneReport) nextReport;
    }

    /**
     * Extracts initial precinct assignments from the first delta.
     *
     * @param initialPrecinctAssignment map whose key is a precinct id and value is a district id
     * @param initialDistricts          map whose key is a district id and value is a district
     * @param precinctAssignments       map whose key is a district id and value is the set of precinct names it contains
     * @param newDistricts              map whose key is a district id and value is a district
     */
    private void extractInitialPrecinctAssignments(Map<String, String> initialPrecinctAssignment,
                                                   Map<String, MergedDistrict> initialDistricts,
                                                   Map<String, Set<String>> precinctAssignments,
                                                   Map<String, MergedDistrict> newDistricts) {
        initialPrecinctAssignment.forEach((precinctId, districtId) -> {
            Set<String> districtPrecincts = new HashSet<>();
            districtPrecincts.add(precinctId);
            precinctAssignments.put(districtId, districtPrecincts);
            newDistricts.put(districtId, initialDistricts.get(districtId));
        });
    }

    /**
     * Updates the precinct assignments map to keep track of which district each precinct has been merged into.
     *
     * @param precinctAssignments map whose key is a district id and value is the set of precinct names it contains
     * @param newDistricts        map whose key is a district id and value is a district
     * @return the number of iterations executed
     */
    private int trackDistrictMerges(Map<String, Set<String>> precinctAssignments,
                                    Map<String, MergedDistrict> newDistricts) {
        PhaseOneMergeDelta currDelta;
        int iteration = 0;
        while (!deltas.isEmpty()) {
            currDelta = deltas.poll();
            iteration = currDelta.getIteration();
            final Map<String, String> mergedDistricts = currDelta.getChangedNodes();
            final Map<String, MergedDistrict> newDeltaDistricts = currDelta.getNewDistricts();
            mergedDistricts.forEach((smallerDistrictId, biggerDistrictId) -> {
                if (smallerDistrictId.equals(biggerDistrictId)) {
                    return;
                }
                Set<String> biggerPrecinctIds = precinctAssignments.get(biggerDistrictId);
                Set<String> smallerPrecinctIds = precinctAssignments.get(smallerDistrictId);
                biggerPrecinctIds.addAll(smallerPrecinctIds);
                precinctAssignments.remove(smallerDistrictId);
                newDistricts.put(biggerDistrictId, newDeltaDistricts.get(biggerDistrictId));
                newDistricts.remove(smallerDistrictId);
            });
        }
        return iteration;
    }

    private Map<String, String> createPrecinctToFinalDistrictMapping(Map<String, Set<String>> precinctAssignments) {
        final Map<String, String> precinctToFinalDistrictMapping = new HashMap<>();
        precinctAssignments.forEach((finalDistrictId, precinctIds) -> {
            precinctIds.forEach(precinctId -> {
                precinctToFinalDistrictMapping.put(precinctId, finalDistrictId);
            });
        });
        return precinctToFinalDistrictMapping;
    }

    /**
     * Aggregates the record of deltas into one delta for a non-iterative final update if specified by the user.
     *
     * @return the same instance containing the aggregated PhaseOneMergeDelta describing the final district each
     * precinct belongs to
     */
    public PhaseOneReport fetchAggregateReport() {
        if (deltas.peek() == null) {
            return null;
        }

        Map<String, Set<String>> precinctAssignments = new HashMap<>();
        Map<String, String> changedNodes;
        Map<String, MergedDistrict> newDistricts = new HashMap<>();

        // Get the initial precinct to district mappings from the 0th delta.
        PhaseOneMergeDelta currDelta = deltas.poll();
        final Map<String, String> initialPrecinctAssignment = currDelta.getChangedNodes();
        final Map<String, MergedDistrict> initialDistricts = currDelta.getNewDistricts();
        this.extractInitialPrecinctAssignments(initialPrecinctAssignment, initialDistricts, precinctAssignments, newDistricts);

        // For each new delta, track the merging of districts.
        int iteration = this.trackDistrictMerges(precinctAssignments, newDistricts);

        // Retrieve the final districts each precinct belongs to.
        changedNodes = this.createPrecinctToFinalDistrictMapping(precinctAssignments);

        PhaseOneMergeDelta aggregateDelta = new PhaseOneMergeDelta(iteration, changedNodes, newDistricts);
        this.deltas.offer(aggregateDelta);
        return this;
    }

}
