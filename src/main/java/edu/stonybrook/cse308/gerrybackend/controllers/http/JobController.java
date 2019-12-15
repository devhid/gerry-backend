package edu.stonybrook.cse308.gerrybackend.controllers.http;

import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.services.DistrictService;
import edu.stonybrook.cse308.gerrybackend.db.services.JobService;
import edu.stonybrook.cse308.gerrybackend.db.services.StateService;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final StateService stateService;
    private final DistrictService districtService;

    @Autowired
    public JobController(JobService jobService, StateService stateService, DistrictService districtService) {
        this.jobService = jobService;
        this.stateService = stateService;
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> list = jobService.getAllJobs();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") String id) {
        Job job = jobService.getJobById(id);
        return new ResponseEntity<>(job, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteJobById(@PathVariable("id") String id) {
        Job job = jobService.getJobById(id);
        StateNode state = job.getState();
        if (state != null) {
            Set<DistrictNode> children = state.clearAndReturnChildren();
            districtService.deleteAllDistricts(children);
            stateService.deleteStateById(id);
        }
        jobService.deleteJobById(id);
        return new ResponseEntity(new HttpHeaders(), HttpStatus.OK);
    }

}
