package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictEdgeRepository;
import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class DistrictService extends EntityService<DistrictNode> {

    private final DistrictRepository districtRepo;
    private final DistrictEdgeRepository districtEdgeRepo;

    @Autowired
    public DistrictService(DistrictRepository districtRepo, DistrictEdgeRepository districtEdgeRepo) {
        this.districtRepo = districtRepo;
        this.districtEdgeRepo = districtEdgeRepo;
    }

    public List<DistrictNode> getAllDistricts() {
        return this.getAllEntities(this.districtRepo);
    }

    public DistrictNode getDistrictById(String id) {
        return this.getEntityById(this.districtRepo, id);
    }

    public DistrictNode createOrUpdateDistrict(DistrictNode district) {
        return this.createOrUpdateEntity(this.districtRepo, district);
    }

    public boolean deleteDistrictById(String id) {
        return this.deleteEntityById(this.districtRepo, id);
    }

    public void deleteAllDistricts(Collection<DistrictNode> districts) {
        districtRepo.deleteAll(districts);
    }

}
