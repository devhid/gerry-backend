package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(cacheNames="states")
    public StateNode getStateById(String id) {
        return this.getEntityById(this.stateRepo, id);
    }

    @CachePut(cacheNames="states", key="#state.id")
    public StateNode createOrUpdateState(StateNode state) {
        List<DistrictNode> allDistricts = new ArrayList<>(state.getChildren());
        districtRepo.saveAll(allDistricts);
        return this.createOrUpdateEntity(this.stateRepo, state);
//        return state;
    }

    @CacheEvict(cacheNames="states")
    public boolean deleteStateById(String id) {
        return this.deleteEntityById(this.stateRepo, id);
    }

    @Cacheable(cacheNames="states")
    public StateNode findOriginalState(StateType stateType, ElectionType electionType) {
        StateNode state = stateRepo.queryFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
        state.getCounties().size();
        state.getMeasures().size();
        state.getChildren().forEach(d -> {
            d.getChildren().forEach(p -> {
                p.getAdjacentEdges().size();
                p.getGeoJson().length();
            });
            d.getAdjacentEdges().size();
            d.getCounties().size();
            d.getMeasures().size();
            d.getIncumbent();
        });
        return state;
    }

    public StateNode findOriginalStateEntityGraph(StateType stateType, ElectionType electionType) {
        return stateRepo.getFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
    }

}
