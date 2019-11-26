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
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

    @Autowired
    private StateService stateService;

    private AlgPhaseReport handle(AlgPhaseInputs inputs) {
        AlgPhaseWorker worker = null;
        if (inputs instanceof PhaseZeroInputs) {
            worker = new PhaseZeroWorker();
        } else if (inputs instanceof PhaseOneInputs) {
            worker = new PhaseOneWorker();
        } else if (inputs instanceof PhaseTwoInputs) {
            switch (((PhaseTwoInputs) inputs).getPhaseTwoDepthHeuristic()) {
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
        } else {
            throw new IllegalArgumentException("Replace this string later!");
        }
        return worker.run(inputs);
    }

    @PostMapping("/phase0")
    public ResponseEntity<PhaseZeroReport> handlePhaseZero(@RequestBody PhaseZeroInputs inputs) {
        inputs.setState(stateService.findOriginalStateByStateType(inputs.getStateType()));
        final PhaseZeroReport report = (PhaseZeroReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase1")
    public ResponseEntity<PhaseOneReport> handlePhaseOne(@RequestBody PhaseOneInputs inputs) {
        inputs.setState(stateService.findOriginalStateByStateType(inputs.getStateType()));
        PhaseOneReport report = (PhaseOneReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/phase2")
    public ResponseEntity<PhaseTwoReport> handlePhaseTwo(@RequestBody PhaseTwoInputs inputs) {
        inputs.setState(stateService.getStateById(inputs.getStateId()));
        PhaseTwoReport report = (PhaseTwoReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

}
