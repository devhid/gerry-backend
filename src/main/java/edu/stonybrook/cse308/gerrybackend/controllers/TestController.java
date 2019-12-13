package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.communication.dto.phaseone.MergedDistrict;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.initializers.PhaseOneReportInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/phase-one-report")
    public ResponseEntity<PhaseOneReport> testPhaseOneReport() {
        Map<String, String> changedNodes = new HashMap<>();
        Map<String, MergedDistrict> newDistricts = new HashMap<>();
        DistrictNode newDistrict = new DistrictNode();
        newDistrict.setNumericalId("0");
        changedNodes.put("Precinct#County", newDistrict.getNumericalId());
        newDistricts.put(newDistrict.getNumericalId(), MergedDistrict.fromDistrictNode(new DistrictNode(), new HashSet<>()));
        PhaseOneMergeDelta delta = new PhaseOneMergeDelta(0, changedNodes, newDistricts);
        Queue<PhaseOneMergeDelta> deltas = new LinkedList<>();
        deltas.offer(delta);
        PhaseOneReport report = PhaseOneReportInitializer.initClass(deltas, "derp");
        report = report.fetchAggregateReport();
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase-two-inputs")
    public ResponseEntity<PhaseTwoInputs> testPhaseTwoInputs(@RequestBody PhaseTwoInputs inputs) {
        return new ResponseEntity<>(inputs, new HttpHeaders(), HttpStatus.OK);
    }

}
