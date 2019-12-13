package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class DistrictService extends EntityService<DistrictNode> {

    private final DistrictRepository repo;

    @Autowired
    public DistrictService(DistrictRepository repo) {
        this.repo = repo;
    }

    public List<DistrictNode> getAllDistricts() {
        return this.getAllEntities(this.repo);
    }

    public DistrictNode getDistrictById(String id) {
        return this.getEntityById(this.repo, id);
    }

    public DistrictNode createOrUpdateDistrict(DistrictNode district) {
        return this.createOrUpdateEntity(this.repo, district);
    }

    public boolean deleteDistrictById(String id) {
        return this.deleteEntityById(this.repo, id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Set<PrecinctNode> fetchDistrictPrecincts(DistrictNode district) {
        district.getChildren().size();
        return district.getChildren();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Set<DistrictEdge> fetchDistrictEdges(DistrictNode district) {
        district.getAdjacentEdges().size();
        return district.getAdjacentEdges();
    }

}
