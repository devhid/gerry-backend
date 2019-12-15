package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.AlgPhaseReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseTwoReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseZeroReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.AlgPhaseWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseOneWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseTwoWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseZeroWorker;
import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.services.DistrictService;
import edu.stonybrook.cse308.gerrybackend.db.services.JobService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgRunType;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/algorithm")
@MessageMapping("/algorithm")
public class AlgorithmController {

    private final StateService stateService;
    private final DistrictService districtService;
    private final JobService jobService;
    private final int initialIteration;
    private final double phaseTwoEpsilon;

    @Autowired
    public AlgorithmController(StateService stateService, DistrictService districtService, JobService jobService,
                               @Value("${gerry.alg.initialIteration}") int initialIteration, @Value("${gerry.phase-two.epsilon}") double phaseTwoEpsilon) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.jobService = jobService;
        this.initialIteration = initialIteration;
        this.phaseTwoEpsilon = phaseTwoEpsilon;
    }

    private AlgPhaseReport handle(AlgPhaseInputs inputs) {
        AlgPhaseWorker worker;
        if (inputs instanceof PhaseZeroInputs) {
            worker = new PhaseZeroWorker();
        } else if (inputs instanceof PhaseOneInputs) {
            worker = new PhaseOneWorker(initialIteration);
        } else if (inputs instanceof PhaseTwoInputs) {
            worker = new PhaseTwoWorker();
        } else {
            throw new IllegalArgumentException("Replace this string later!");
        }
        return worker.run(inputs);
    }

    private PhaseOneReport phaseOneStart(PhaseOneInputs inputs) {
        StateNode stateNode = stateService.findOriginalState(inputs.getStateType(), inputs.getElectionType());
        inputs.setState(stateNode);
        return (PhaseOneReport) this.handle(inputs);
    }

    private PhaseOneReport phaseOneIterative(PhaseOneInputs inputs) {
        Job job = jobService.getJobById(inputs.getJobId());
        StateNode state = stateService.getStateById(inputs.getJobId());
        state.fillInTransientProperties();
        inputs.setJob(job);
        inputs.setState(state);
        return (PhaseOneReport) this.handle(inputs);
    }

    private PhaseZeroReport handlePhaseZeroHelper(PhaseZeroInputs inputs) {
        inputs.setState(stateService.findOriginalState(inputs.getStateType(), inputs.getElectionType()));
        return (PhaseZeroReport) handle(inputs);
    }

    @PostMapping("/phase0")
    public ResponseEntity<PhaseZeroReport> handlePhaseZeroHTTP(@RequestBody PhaseZeroInputs inputs) {
        final PhaseZeroReport report = handlePhaseZeroHelper(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @MessageMapping("/phase0")
    @SendToUser("/queue/reports/phase0")
    @Transactional(propagation = Propagation.REQUIRED)
    public PhaseZeroReport handlePhaseZeroWS(PhaseZeroInputs inputs) {
        return handlePhaseZeroHelper(inputs);
    }

    public PhaseOneReport handlePhaseOneHelper(PhaseOneInputs inputs) {
        PhaseOneReport report;
        Job job;
        if (inputs.getJobId() == null) {
            report = phaseOneStart(inputs);
            job = inputs.getJob();
        } else {
            report = phaseOneIterative(inputs);
            job = inputs.getJob();
        }
        // TODO: need to fix this method
//        if (inputs.getAlgRunType() == AlgRunType.TO_COMPLETION) {
//            report = report.fetchAggregateReport();
//        }
        stateService.createOrUpdateState(inputs.getState());
        jobService.createOrUpdateJob(job);
        return report;
    }

    @PostMapping("/phase1")
    public ResponseEntity<PhaseOneReport> handlePhaseOneHTTP(@RequestBody PhaseOneInputs inputs) {
        PhaseOneReport report = handlePhaseOneHelper(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

    @MessageMapping("/phase1")
    @SendToUser("/queue/reports/phase1")
    @Transactional(propagation = Propagation.REQUIRED)
    public PhaseOneReport handlePhaseOneWS(PhaseOneInputs inputs) {
        return handlePhaseOneHelper(inputs);
    }

    @PostMapping("/phase2")
    public ResponseEntity<PhaseTwoReport> handlePhaseTwo(@RequestBody PhaseTwoInputs inputs) {
        Job phaseOneJob = jobService.getJobById(inputs.getPhaseOneJobId());
        inputs.setState(phaseOneJob.getState());
        inputs.setEpsilon(this.phaseTwoEpsilon);
        PhaseTwoReport report = (PhaseTwoReport) handle(inputs);
        return new ResponseEntity<>(report, new HttpHeaders(), HttpStatus.OK);
    }

}
