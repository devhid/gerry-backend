package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseZeroWorker;
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
    @PostMapping("/phase0")
    public ResponseEntity<Set<PhaseZeroResult>> getPrecinctBlocs(@RequestBody PhaseZeroInputs phaseZeroInputs) {
        final PhaseZeroWorker phaseZeroWorker = new PhaseZeroWorker();
        final Set<PhaseZeroResult> phaseZeroReport = phaseZeroWorker.run(phaseZeroInputs).getPhaseZeroResults();
        return new ResponseEntity<>(phaseZeroReport, new HttpHeaders(), HttpStatus.OK);
    }
}
