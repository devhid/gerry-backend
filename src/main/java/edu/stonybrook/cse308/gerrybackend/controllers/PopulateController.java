package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.data.graph.DemographicData;
import edu.stonybrook.cse308.gerrybackend.data.graph.ElectionData;
import edu.stonybrook.cse308.gerrybackend.data.pairs.UnorderedPair;
import edu.stonybrook.cse308.gerrybackend.db.services.DistrictService;
import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.enums.types.*;
import edu.stonybrook.cse308.gerrybackend.exceptions.InvalidEdgeException;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.GerryNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.utils.GenericUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/populate")
public class PopulateController {

    private final PrecinctService precinctService;
    private final DistrictService districtService;
    private final StateService stateService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateController.class);

    @Autowired
    public PopulateController(PrecinctService precinctService, DistrictService districtService, StateService stateService) {
        this.precinctService = precinctService;
        this.districtService = districtService;
        this.stateService = stateService;
    }

    private void createOrUpdateEntity(GerryNode node) {
        if (node instanceof PrecinctNode) {
            LOGGER.info("Creating new precinct " + node.getId());
            precinctService.createOrUpdatePrecinct((PrecinctNode) node);
        } else if (node instanceof DistrictNode) {
            LOGGER.info("Creating new district " + node.getId());
            districtService.createOrUpdateDistrict((DistrictNode) node);
        } else if (node instanceof StateNode) {
            LOGGER.info("Creating new state " + node.getId());
            stateService.createOrUpdateState((StateNode) node);
        }
    }

    private void populateAdjDistrictPairs(StateNode state, Set<UnorderedPair<DistrictNode>> adjDistricts) {
        state.getChildren().forEach(d -> {
            Set<PrecinctNode> borderPrecincts = d.getBorderPrecincts();
            borderPrecincts.forEach(borderPrecinct -> {
                Set<PrecinctNode> adjToBorderPrecincts = GenericUtils.castSetOfObjects(borderPrecinct.getAdjacentNodes(),
                        PrecinctNode.class);
                adjToBorderPrecincts.forEach(adjBorderPrecinct -> {
                    DistrictNode adjBorderPrecinctDistrict = adjBorderPrecinct.getParent();
                    if (adjBorderPrecinctDistrict == d) {
                        return;
                    }
                    UnorderedPair<DistrictNode> districtPair = new UnorderedPair<>(d, adjBorderPrecinctDistrict);
                    adjDistricts.add(districtPair);
                });
            });
        });
    }

    private void createDistrictEdgeRefs(Set<UnorderedPair<DistrictNode>> adjDistricts) {
        adjDistricts.forEach(districtPair -> {
            DistrictNode d1 = districtPair.getItem1();
            DistrictNode d2 = districtPair.getItem2();
            DistrictEdge edge = new DistrictEdge(UUID.randomUUID().toString(), d1, d2);
            try {
                d1.addEdge(edge);
                d2.addEdge(edge);
            } catch (InvalidEdgeException e) {
                e.printStackTrace();
            }
        });
    }

    private void populateDistrictEdges(StateNode state) {
        state.fillInTransientProperties();
        Set<UnorderedPair<DistrictNode>> adjDistricts = new HashSet<>();
        this.populateAdjDistrictPairs(state, adjDistricts);
        this.createDistrictEdgeRefs(adjDistricts);
    }

    @PostMapping("/echo-precinct")
    public ResponseEntity<PrecinctNode> echoPrecinct(@RequestBody PrecinctNode precinct) {
        return new ResponseEntity<>(precinct, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/echo-district")
    public ResponseEntity<DistrictNode> echoDistrict(@RequestBody DistrictNode district) {
        return new ResponseEntity<>(district, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/echo-state")
    public ResponseEntity<StateNode> echoState(@RequestBody StateNode state) {
        state.fillInTransientProperties();
        this.populateDistrictEdges(state);
        return new ResponseEntity<>(state, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/create-precinct")
    public ResponseEntity<PrecinctNode> createPrecinct(@RequestBody PrecinctNode precinct) {
        createOrUpdateEntity(precinct);
        return new ResponseEntity<>(precinct, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/create-district")
    public ResponseEntity<DistrictNode> createDistrict(@RequestBody DistrictNode district) {
        createOrUpdateEntity(district);
        return new ResponseEntity<>(district, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/create-state")
    public ResponseEntity<StateNode> createState(@RequestBody StateNode state) {
        state.fillInTransientProperties();
        state.aggregateStatistics();
        this.populateDistrictEdges(state);
        state.getMultiPolygon();
        createOrUpdateEntity(state);
        return new ResponseEntity<>(state, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/precinct/{id}")
    public ResponseEntity deletePrecinctById(@PathVariable String id) {
        precinctService.deletePrecinctById(id);
        return new ResponseEntity(new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/district/{id}")
    public ResponseEntity deleteDistrictById(@PathVariable String id) {
        districtService.deleteDistrictById(id);
        return new ResponseEntity(new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/state/{id}")
    public ResponseEntity deleteStateById(@PathVariable String id) {
        StateNode state = stateService.getStateById(id);
        if (state != null) {
            Set<DistrictNode> children = state.clearAndReturnChildren();
            for (DistrictNode district : children) {
                districtService.deleteDistrictById(district.getId());
            }
            stateService.deleteStateById(id);
        }
        return new ResponseEntity(new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/new-test-state")
    public ResponseEntity<StateNode> getNewTestState() throws InvalidEdgeException {
        // Create demographic data.
        Map<DemographicType, Integer> pop1 = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> votingAgePop1 = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> pop2 = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> votingAgePop2 = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> pop3 = new EnumMap<>(DemographicType.class);
        Map<DemographicType, Integer> votingAgePop3 = new EnumMap<>(DemographicType.class);

        pop1.put(DemographicType.NH_WHITE, 100);
        votingAgePop1.put(DemographicType.NH_WHITE, 50);
        pop2.put(DemographicType.NH_WHITE, 200);
        votingAgePop2.put(DemographicType.NH_WHITE, 100);
        pop3.put(DemographicType.NH_WHITE, 300);
        votingAgePop3.put(DemographicType.NH_WHITE, 150);

        DemographicData demo1 = new DemographicData(UUID.randomUUID().toString(), pop1, votingAgePop1);
        DemographicData demo2 = new DemographicData(UUID.randomUUID().toString(), pop2, votingAgePop2);
        DemographicData demo3 = new DemographicData(UUID.randomUUID().toString(), pop3, votingAgePop3);

        // Create election data.
        ElectionType electionType = ElectionType.PRESIDENTIAL_2016;
        Map<PoliticalParty, Integer> votes1 = new EnumMap<>(PoliticalParty.class);
        Map<PoliticalParty, Integer> votes2 = new EnumMap<>(PoliticalParty.class);
        Map<PoliticalParty, Integer> votes3 = new EnumMap<>(PoliticalParty.class);

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
        PrecinctNode p1 = PrecinctNode.builder()
                .id(UUID.randomUUID().toString())
                .name("p1")
                .demographicData(demo1)
                .electionData(election1)
                .adjacentEdges(new HashSet<>())
                .geography("{}")
                .county("p1-county")
                .build();
        PrecinctNode p2 = PrecinctNode.builder()
                .id(UUID.randomUUID().toString())
                .name("p2")
                .demographicData(demo2)
                .electionData(election2)
                .adjacentEdges(new HashSet<>())
                .geography("{}")
                .county("p2-county")
                .build();
        PrecinctNode p3 = PrecinctNode.builder()
                .id(UUID.randomUUID().toString())
                .name("p3")
                .demographicData(demo3)
                .electionData(election3)
                .adjacentEdges(new HashSet<>())
                .geography("{}")
                .county("p3-county")
                .build();

        PrecinctEdge p1p2Edge = new PrecinctEdge(UUID.randomUUID().toString(), p1, p2);
        PrecinctEdge p1p3Edge = new PrecinctEdge(UUID.randomUUID().toString(), p1, p3);
        PrecinctEdge p2p3Edge = new PrecinctEdge(UUID.randomUUID().toString(), p2, p3);

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

        DistrictNode d1 = DistrictNode.builder()
                .id(UUID.randomUUID().toString())
                .name("d1")
                .nodeType(NodeType.ORIGINAL)
                .precincts(d1Precincts)
                .geography("{}")
                .adjacentEdges(new HashSet<>())
                .build();
        DistrictNode d2 = DistrictNode.builder()
                .id(UUID.randomUUID().toString())
                .name("d2")
                .nodeType(NodeType.ORIGINAL)
                .precincts(d2Precincts)
                .geography("{}")
                .adjacentEdges(new HashSet<>())
                .build();

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

        StateNode state = StateNode.builder()
                .id(UUID.randomUUID().toString())
                .name("state")
                .nodeType(NodeType.ORIGINAL)
                .districts(districts)
                .geography("{}")
                .counties(counties)
                .stateType(StateType.CALIFORNIA)
                .build();
        stateService.createOrUpdateState(state);
        return new ResponseEntity<>(state, new HttpHeaders(), HttpStatus.OK);
    }

}
