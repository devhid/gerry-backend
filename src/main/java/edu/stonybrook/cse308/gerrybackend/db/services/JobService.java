package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService extends EntityService<Job> {

    private final JobRepository repo;

    @Autowired
    public JobService(JobRepository repo) {
        this.repo = repo;
    }

    public List<Job> getAllJobs() {
        return this.getAllEntities(this.repo);
    }

    public Job getJobById(String id) {
        return this.getEntityById(this.repo, id);
    }

    public Job createOrUpdateJob(Job job) {
        return this.createOrUpdateEntity(this.repo, job);
    }

    public boolean deleteJobById(String id) {
        return this.deleteEntityById(this.repo, id);
    }

}