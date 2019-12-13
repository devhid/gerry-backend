package edu.stonybrook.cse308.gerrybackend.controllers.sockets;

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
import edu.stonybrook.cse308.gerrybackend.db.services.JobService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Controller
@MessageMapping("/algorithm")
public class AlgorithmSocketController {

    private StateService stateService;
    private JobService jobService;

    @Autowired
    public AlgorithmSocketController(StateService stateService, JobService jobService) {
        this.stateService = stateService;
        this.jobService = jobService;
    }

    private AlgPhaseReport handle(AlgPhaseInputs inputs) {
        AlgPhaseWorker worker;
        if (inputs instanceof PhaseZeroInputs) {
            worker = new PhaseZeroWorker();
        } else if (inputs instanceof PhaseOneInputs) {
            worker = new PhaseOneWorker();
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
        inputs.setJob(job);
        inputs.setState(job.getState());
        return (PhaseOneReport) this.handle(inputs);
    }

    @MessageMapping("/phase0")
    @SendToUser("/queue/reports/phase0")
    @Transactional(propagation = Propagation.REQUIRED)
    public PhaseZeroReport handlePhaseZero(PhaseZeroInputs inputs) {
        inputs.setState(stateService.findOriginalState(inputs.getStateType(), inputs.getElectionType()));
        return (PhaseZeroReport) handle(inputs);
    }

    @MessageMapping("/phase1")
    @SendToUser("/queue/reports/phase1")
    @Transactional(propagation = Propagation.REQUIRED)
    public PhaseOneReport handlePhaseOne(PhaseOneInputs inputs) {
        PhaseOneReport report;
        if (inputs.getJobId() == null) {
            report = phaseOneStart(inputs);
            Job job = new Job(AlgPhaseType.PHASE_ONE, inputs.getState());
            report.setJobId(job.getId());
        } else {
            report = phaseOneIterative(inputs);
        }
        return report;
    }

    @MessageMapping("/phase2")
    @SendTo("/queue/reports/phase2")
    @Transactional(propagation = Propagation.REQUIRED)
    public PhaseTwoReport handlePhaseTwo(PhaseTwoInputs inputs) {
        PhaseTwoReport report = null;
        return report;
    }

}
