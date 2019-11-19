package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/precincts")
public class PrecinctController {

    @Autowired
    PrecinctService precinctService;

    @GetMapping
    public ResponseEntity<List<PrecinctNode>> getAllPrecincts() {
        List<PrecinctNode> list = precinctService.getAllPrecincts();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecinctNode> getPrecinctById(@PathVariable("id") String id) {
        PrecinctNode precinct = precinctService.getPrecinctById(id);
        return new ResponseEntity<>(precinct, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<PrecinctNode> getEmptyPrecinct(){
        PrecinctNode precinct = new PrecinctNode();
        precinctService.createPrecinct(precinct);
        return new ResponseEntity<>(precinct, new HttpHeaders(), HttpStatus.OK);
    }

}
