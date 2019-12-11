package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.communication.InitialData;
import edu.stonybrook.cse308.gerrybackend.data.reports.AggregateInfo;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
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
    public ResponseEntity<StateNode> getOriginalState(@PathVariable StateType stateType, @PathVariable ElectionType electionType) {
        StateNode originalState = stateService.findOriginalState(stateType, electionType);
        if (originalState != null) {
            originalState.fillInTransientProperties();
        }
        return new ResponseEntity<>(originalState, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/data/{stateType}")
    public ResponseEntity<InitialData> getStateData(@PathVariable StateType stateType) {
        final StateNode statePres16 = stateService.findOriginalState(stateType, ElectionType.PRESIDENTIAL_2016);
        final StateNode stateHouse16 = stateService.findOriginalState(stateType, ElectionType.CONGRESSIONAL_2016);
        final StateNode stateHouse18 = stateService.findOriginalState(stateType, ElectionType.CONGRESSIONAL_2018);

        InitialData initialData = new InitialData();
        initialData.populateStateInfo(statePres16, stateHouse16, stateHouse18);


        return new ResponseEntity<>(initialData, new HttpHeaders(), HttpStatus.OK);
    }

}
