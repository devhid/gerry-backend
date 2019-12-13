package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PrecinctService extends EntityService<PrecinctNode> {

    private final PrecinctRepository repo;

    @Autowired
    public PrecinctService(PrecinctRepository repo) {
        this.repo = repo;
    }

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

    @Transactional(propagation = Propagation.REQUIRED)
    public Set<PrecinctEdge> fetchPrecinctEdges(PrecinctNode precinct) {
        precinct.getAdjacentEdges().size();
        return precinct.getAdjacentEdges();
    }

}
