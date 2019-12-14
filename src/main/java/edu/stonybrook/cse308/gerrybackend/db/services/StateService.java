package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class StateService extends EntityService<StateNode> {

    private final StateRepository stateRepo;
    private final DistrictRepository districtRepo;
    private final EntityManager entityManager;
    private final int BATCH_SIZE = 100;

    @Autowired
    public StateService(StateRepository stateRepo, DistrictRepository districtRepo, EntityManager entityManager) {
        this.stateRepo = stateRepo;
        this.districtRepo = districtRepo;
        this.entityManager = entityManager;
    }

    public List<StateNode> getAllStates() {
        return this.getAllEntities(this.stateRepo);
    }

    public StateNode getStateById(String id) {
        return this.getEntityById(this.stateRepo, id);
    }

    public StateNode createOrUpdateState(StateNode state) {
        List<DistrictNode> allDistricts = new ArrayList<>(state.getChildren());
        districtRepo.saveAll(allDistricts);
//        for (DistrictNode district : state.getChildren()) {
//            districtRepo.save(district);
//        }
//        int iteration = 0;
//        for (DistrictNode district : state.getChildren()) {
//            if (iteration > 0 && iteration % BATCH_SIZE == 0) {
//                entityManager.flush();
//                entityManager.clear();
//            }
//            districtRepo.save(district);
//            iteration++;
//        }
        return this.createOrUpdateEntity(this.stateRepo, state);
    }

    public boolean deleteStateById(String id) {
        return this.deleteEntityById(this.stateRepo, id);
    }

    public StateNode findOriginalState(StateType stateType, ElectionType electionType) {
        return stateRepo.queryFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
    }

    public StateNode findOriginalStateEntityGraph(StateType stateType, ElectionType electionType) {
        return stateRepo.getFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
    }

}
