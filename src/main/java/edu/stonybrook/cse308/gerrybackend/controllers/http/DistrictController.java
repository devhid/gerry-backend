package edu.stonybrook.cse308.gerrybackend.controllers.http;

import edu.stonybrook.cse308.gerrybackend.db.services.DistrictService;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
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
@RequestMapping("/districts")
public class DistrictController {

    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<List<DistrictNode>> getAllDistricts() {
        List<DistrictNode> list = districtService.getAllDistricts();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictNode> getPrecinctById(@PathVariable("id") String id) {
        DistrictNode district = districtService.getDistrictById(id);
        return new ResponseEntity<>(district, new HttpHeaders(), HttpStatus.OK);
    }

}
