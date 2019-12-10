package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.data.reports.PhaseOneMergeDelta;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.initializers.PhaseOneReportInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/phase-one-report")
    public ResponseEntity<PhaseOneReport> testPhaseOneReport() {
        Map<String, String> changedNodes = new HashMap<>();
        Map<String, DistrictNode> newDistricts = new HashMap<>();
        changedNodes.put("precinctA", "districtA");
        newDistricts.put("districtA", new DistrictNode());
        PhaseOneMergeDelta delta = new PhaseOneMergeDelta(0, changedNodes, newDistricts);
        Queue<PhaseOneMergeDelta> deltas = new LinkedList<>();
        deltas.offer(delta);
        PhaseOneReport report = PhaseOneReportInitializer.initClass("derp", deltas);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase-two-inputs")
    public ResponseEntity<PhaseTwoInputs> testPhaseTwoInputs(@RequestBody PhaseTwoInputs inputs) {
        return new ResponseEntity<>(inputs, new HttpHeaders(), HttpStatus.OK);
    }

}
