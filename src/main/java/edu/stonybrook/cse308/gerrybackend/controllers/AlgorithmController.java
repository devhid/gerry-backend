package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.AlgPhaseReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.*;
import edu.stonybrook.cse308.gerrybackend.controllers.mappings.PhaseZeroResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

    private AlgPhaseReport handle(AlgPhaseInputs inputs){
        AlgPhaseWorker worker = null;
        if (inputs instanceof PhaseZeroInputs){
            worker = new PhaseZeroWorker();
        }
        else if (inputs instanceof PhaseOneInputs){
            worker = new PhaseOneWorker();
        }
        else if (inputs instanceof PhaseTwoInputs){
            switch (((PhaseTwoInputs) inputs).getPhaseTwoDepthHeuristic()){
                case STANDARD:
                    worker = new PhaseTwoStandardWorker();
                    break;
                case LEVEL:
                    worker = new PhaseTwoLevelWorker();
                    break;
                case TREE:
                    worker = new PhaseTwoTreeWorker();
                    break;
            }
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
        return worker.run(inputs);
    }

    @PostMapping("/phase0")
    public ResponseEntity<Set<PhaseZeroResult>> handlePhaseZero(@RequestBody PhaseZeroInputs inputs) {
        final PhaseZeroReport report = (PhaseZeroReport) handle(inputs);
        final Set<PhaseZeroResult> results = report.getPhaseZeroResults();
        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase1")
    public ResponseEntity<PhaseOneReport> handlePhaseOne(@RequestBody PhaseOneInputs inputs) {
        PhaseOneReport report = (PhaseOneReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase2")
    public ResponseEntity<PhaseTwoReport> handlePhaseTwo(@RequestBody PhaseTwoInputs inputs) {
        PhaseTwoReport report = (PhaseTwoReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }


}
