package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService extends EntityService<DistrictNode> {

    @Autowired
    private DistrictRepository repo;

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

}
