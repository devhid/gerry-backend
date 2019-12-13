package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.AlgPhaseInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseTwoInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseZeroInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.AlgPhaseReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.AlgPhaseWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseOneWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseTwoWorker;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseZeroWorker;
import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.services.JobService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class AlgorithmSocketController {

    private StateService stateService;
    private JobService jobService;
    private SessionFactory sessionFactory;

    @Autowired
    public AlgorithmSocketController(StateService stateService, JobService jobService, EntityManagerFactory emf) {
        this.stateService = stateService;
        this.jobService = jobService;
        this.sessionFactory = emf.unwrap(SessionFactory.class);
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

    private PhaseOneReport startPhaseOne(PhaseOneInputs inputs) {
        long time = System.currentTimeMillis();
        StateNode stateNode = stateService.findOriginalState(inputs.getStateType(), inputs.getElectionType());
        System.out.println(System.currentTimeMillis() - time);
        inputs.setState(stateNode);
        return (PhaseOneReport) this.handle(inputs);
    }

    private PhaseOneReport iterativePhaseOne(PhaseOneInputs inputs) {
        Job job = jobService.getJobById(inputs.getJobId());
        inputs.setJob(job);
        inputs.setState(job.getState());
        return (PhaseOneReport) this.handle(inputs);
    }

    public PhaseOneReport handlePhaseOne(PhaseOneInputs inputs) {
        PhaseOneReport report;
        if (inputs.getJobId() == null) {
            report = startPhaseOne(inputs);
            Job job = new Job(AlgPhaseType.PHASE_ONE, inputs.getState());
            report.setJobId(job.getId());
        } else {
            report = iterativePhaseOne(inputs);
        }
        return report;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public PhaseOneReport handlePhaseOneAlt(PhaseOneInputs inputs) {
        return handlePhaseOne(inputs);
    }
}
