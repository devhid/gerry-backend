package edu.stonybrook.cse308.gerrybackend.controllers;

import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
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
@RequestMapping("/precincts")
public class PrecinctController {

    @Autowired
    PrecinctService precinctService;

    @GetMapping
    public ResponseEntity<List<PrecinctNode>> getAllEmployees() {
        List<PrecinctNode> list = precinctService.getAllPrecincts();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecinctNode> getEmployeeById(@PathVariable("id") String id) {
        PrecinctNode precinct = precinctService.getPrecinctById(id);
        return new ResponseEntity<>(precinct, new HttpHeaders(), HttpStatus.OK);
    }
}
