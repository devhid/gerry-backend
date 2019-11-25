package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecinctService extends EntityService<PrecinctNode> {

    @Autowired
    private PrecinctRepository repo;

    public List<PrecinctNode> getAllPrecincts() {
        return this.getAllEntities(this.repo);
    }

    public PrecinctNode getPrecinctById(String id) {
        return this.getEntityById(this.repo, id);
    }

    public PrecinctNode createOrUpdatePrecinct(PrecinctNode precinct) {
        return this.createOrUpdateEntity(this.repo, precinct);
    }

    public boolean deletePrecinctById(String id) {
        return this.deleteEntityById(this.repo, id);
    }

}
