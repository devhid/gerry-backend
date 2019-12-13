package edu.stonybrook.cse308.gerrybackend.controllers.http;

import edu.stonybrook.cse308.gerrybackend.communication.dto.statistics.ClusterNodeStatistics;
import edu.stonybrook.cse308.gerrybackend.communication.dto.statistics.StateNodeStatistics;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.ClusterNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {

    private final StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping
    public ResponseEntity<List<StateNode>> getAllStates() {
        List<StateNode> list = stateService.getAllStates();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/original/{stateType}/{electionType}")
    public ResponseEntity<StateNode> getOriginalState(@PathVariable StateType stateType,
                                                      @PathVariable ElectionType electionType) {
        StateNode originalState = stateService.findOriginalState(stateType, electionType);
        return new ResponseEntity<>(originalState, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/original/stats/{stateType}/{electionType}")
    public ResponseEntity<StateNodeStatistics> getOriginalStateStats(@PathVariable StateType stateType,
                                                                     @PathVariable ElectionType electionType) {
        StateNode originalState = stateService.findOriginalState(stateType, electionType);
        StateNodeStatistics stateStats = StateNodeStatistics.fromStateNode(originalState);
        return new ResponseEntity<>(stateStats, new HttpHeaders(), HttpStatus.OK);
    }
}
