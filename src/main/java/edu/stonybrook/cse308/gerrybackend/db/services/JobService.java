package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.data.jobs.Job;
import edu.stonybrook.cse308.gerrybackend.db.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(cacheNames="jobs")
    public Job getJobById(String id) {
        return this.getEntityById(this.repo, id);
    }

    @CachePut(cacheNames="jobs", key="#job.id")
    public Job createOrUpdateJob(Job job) {
        return job;
//        return this.createOrUpdateEntity(this.repo, job);
    }

    @CacheEvict(cacheNames="jobs")
    public boolean deleteJobById(String id) {
        return this.deleteEntityById(this.repo, id);
    }

}