package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.*;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/states")
public class StateController {

    @Autowired
    StateService stateService;

    @GetMapping
    public ResponseEntity<List<StateNode>> getAllStates() {
        List<StateNode> list = stateService.getAllStates();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/original")
    public ResponseEntity<StateNode> getOriginalState(@RequestBody StateType stateType){
        StateNode originalState = stateService.findOriginalStateByStateType(stateType);
        return new ResponseEntity<>(originalState, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/echo")
    public ResponseEntity<StateNode> echoState(@RequestBody StateNode state){
        System.out.println("in echoState");
        state.fillInTransientProperties();
        Set<DistrictNode> districts = state.getNodes();
        districts.forEach(d -> {
            Set<GerryNode> adjNodes = d.getAdjacentNodes();
            adjNodes.forEach(n -> {
                System.out.println("district " + d.getId() + " is adj to " + n.getId());
            });
        });
        Set<PrecinctNode> precincts = state.getAllPrecincts();
        precincts.forEach(p -> {
            Set<GerryNode> adjNodes = p.getAdjacentNodes();
            adjNodes.forEach(n -> {
                System.out.println("precinct " + p.getId() + " is adj to " + n.getId());
            });
        });
        return new ResponseEntity<StateNode>(state, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/new_test")
    public ResponseEntity<StateNode> getNewTestState() throws MismatchedElectionException, InvalidEdgeException {
        // Create demographic data.
        Map<DemographicType,Integer> pop1 = new EnumMap<>(DemographicType.class);
        Map<DemographicType,Integer> votingAgePop1 = new EnumMap<>(DemographicType.class);
        Map<DemographicType,Integer> pop2 = new EnumMap<>(DemographicType.class);
        Map<DemographicType,Integer> votingAgePop2 = new EnumMap<>(DemographicType.class);
        Map<DemographicType,Integer> pop3 = new EnumMap<>(DemographicType.class);
        Map<DemographicType,Integer> votingAgePop3 = new EnumMap<>(DemographicType.class);

        pop1.put(DemographicType.ALL, 100);
        votingAgePop1.put(DemographicType.ALL, 50);
        pop2.put(DemographicType.ALL, 200);
        votingAgePop2.put(DemographicType.ALL, 100);
        pop3.put(DemographicType.ALL, 300);
        votingAgePop3.put(DemographicType.ALL, 150);

        DemographicData demo1 = new DemographicData(UUID.randomUUID().toString(), pop1, votingAgePop1);
        DemographicData demo2 = new DemographicData(UUID.randomUUID().toString(), pop2, votingAgePop2);
        DemographicData demo3 = new DemographicData(UUID.randomUUID().toString(), pop3, votingAgePop3);

        // Create election data.
        ElectionType electionType = ElectionType.PRESIDENTIAL_2016;
        Map<PoliticalParty,Integer> votes1 = new EnumMap<>(PoliticalParty.class);
        Map<PoliticalParty,Integer> votes2 = new EnumMap<>(PoliticalParty.class);
        Map<PoliticalParty,Integer> votes3 = new EnumMap<>(PoliticalParty.class);

        votes1.put(PoliticalParty.DEMOCRATIC, 0);
        votes1.put(PoliticalParty.REPUBLICAN, 50);
        votes1.put(PoliticalParty.INDEPENDENT, 0);
        votes1.put(PoliticalParty.OTHER, 0);
        votes2.put(PoliticalParty.DEMOCRATIC, 0);
        votes2.put(PoliticalParty.REPUBLICAN, 0);
        votes2.put(PoliticalParty.INDEPENDENT, 100);
        votes2.put(PoliticalParty.OTHER, 0);
        votes3.put(PoliticalParty.DEMOCRATIC, 150);
        votes3.put(PoliticalParty.REPUBLICAN, 0);
        votes3.put(PoliticalParty.INDEPENDENT, 0);
        votes3.put(PoliticalParty.OTHER, 0);

        ElectionData election1 = new ElectionData(UUID.randomUUID().toString(), electionType, votes1, PoliticalParty.REPUBLICAN);
        ElectionData election2 = new ElectionData(UUID.randomUUID().toString(), electionType, votes2, PoliticalParty.INDEPENDENT);
        ElectionData election3 = new ElectionData(UUID.randomUUID().toString(), electionType, votes3, PoliticalParty.DEMOCRATIC);

        // Create precincts
        PrecinctNode p1 = new PrecinctNode(UUID.randomUUID().toString(), "p1", demo1, election1, new HashSet<>(), "{}", "p1-county", null);
        PrecinctNode p2 = new PrecinctNode(UUID.randomUUID().toString(), "p2", demo2, election2, new HashSet<>(), "{}", "p2-county", null);
        PrecinctNode p3 = new PrecinctNode(UUID.randomUUID().toString(), "p3", demo3, election3, new HashSet<>(), "{}", "p3-county", null);

        PrecinctEdge p1p2Edge = new PrecinctEdge(UUID.randomUUID().toString(),p1,p2);
        PrecinctEdge p1p3Edge = new PrecinctEdge(UUID.randomUUID().toString(),p1,p3);
        PrecinctEdge p2p3Edge = new PrecinctEdge(UUID.randomUUID().toString(),p2,p3);

        p1.addEdge(p1p2Edge);
        p1.addEdge(p1p3Edge);
        p2.addEdge(p1p2Edge);
        p2.addEdge(p2p3Edge);
        p3.addEdge(p1p3Edge);
        p3.addEdge(p2p3Edge);

        // Create districts
        Set<PrecinctNode> d1Precincts = new HashSet<>();
        Set<PrecinctNode> d2Precincts = new HashSet<>();
        d1Precincts.add(p1);
        d1Precincts.add(p2);
        d2Precincts.add(p3);

        DistrictNode d1 = new DistrictNode(UUID.randomUUID().toString(), "d1", NodeType.ORIGINAL, d1Precincts, "{}");
        DistrictNode d2 = new DistrictNode(UUID.randomUUID().toString(), "d2", NodeType.ORIGINAL, d2Precincts, "{}");

        DistrictEdge d1d2Edge = new DistrictEdge(UUID.randomUUID().toString(), d1, d2);
        d1.addEdge(d1d2Edge);
        d2.addEdge(d1d2Edge);

        // Create state.
        Set<DistrictNode> districts = new HashSet<>();
        districts.add(d1);
        districts.add(d2);
        Set<String> counties = new HashSet<>();
        counties.add(p1.getCounty());
        counties.add(p2.getCounty());
        counties.add(p3.getCounty());

        StateNode state = new StateNode(UUID.randomUUID().toString(), "state", NodeType.ORIGINAL, districts, "{}", counties, StateType.CALIFORNIA);
//        stateService.createState(state);
        return new ResponseEntity<>(state, new HttpHeaders(), HttpStatus.OK);
    }

}
