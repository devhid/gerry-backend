package edu.stonybrook.cse308.gerrybackend.controllers.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stonybrook.cse308.gerrybackend.algorithms.inputs.PhaseOneInputs;
import edu.stonybrook.cse308.gerrybackend.algorithms.reports.PhaseOneReport;
import edu.stonybrook.cse308.gerrybackend.algorithms.workers.PhaseOneWorker;
import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.services.JobService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class PhaseOneSocketHandler extends TextWebSocketHandler {

    private ObjectMapper objectMapper;
    private StateService stateService;
    private JobService jobService;

    final static Logger LOGGER = LoggerFactory.getLogger(PhaseOneSocketHandler.class);

    @Autowired
    public PhaseOneSocketHandler(ObjectMapper objectMapper, StateService stateService, JobService jobService) {
        this.objectMapper = objectMapper;
        this.stateService = stateService;
        this.jobService = jobService;
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOGGER.info(String.format("Connected to session %s for Phase 1", session.getId()));
    }

    private PhaseOneReport handle(PhaseOneInputs inputs) {
        return new PhaseOneWorker().run(inputs);
    }

    private PhaseOneReport startPhaseOne(PhaseOneInputs inputs) {
        long time = System.currentTimeMillis();
        StateNode stateNode = stateService.findOriginalState(inputs.getStateType(), inputs.getElectionType());
        System.out.println(System.currentTimeMillis() - time);
        inputs.setState(stateNode);
        return this.handle(inputs);
    }

    private PhaseOneReport iterativePhaseOne(PhaseOneInputs inputs) {
        Job job = jobService.getJobById(inputs.getJobId());
        inputs.setJob(job);
        inputs.setState(job.getState());
        return this.handle(inputs);
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage json) throws Exception {
        LOGGER.info(String.format("Received message: %s", json.getPayload()));
        PhaseOneInputs inputs = objectMapper.readValue(json.getPayload(), PhaseOneInputs.class);
        PhaseOneReport report;
        if (inputs.getJobId() == null) {
            report = startPhaseOne(inputs);
            Job job = new Job(AlgPhaseType.PHASE_ONE, inputs.getState());
            report.setJobId(job.getId());
        } else {
            report = iterativePhaseOne(inputs);
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(report)));
    }

    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable throwable) throws Exception {
        LOGGER.error(String.format("Error from sender %s", session.toString()), throwable);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        LOGGER.info(String.format("Session %s closed because of %s.", session.getId(), status.getReason()));
    }

}
