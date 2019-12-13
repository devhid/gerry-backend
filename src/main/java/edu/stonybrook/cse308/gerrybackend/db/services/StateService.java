package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class StateService extends EntityService<StateNode> {

    private final StateRepository repo;

    @Autowired
    public StateService(StateRepository repo) {
        this.repo = repo;
    }

    public List<StateNode> getAllStates() {
        return this.getAllEntities(this.repo);
    }

    public StateNode getStateById(String id) {
        return this.getEntityById(this.repo, id);
    }

    public StateNode createOrUpdateState(StateNode state) {
        return this.createOrUpdateEntity(this.repo, state);
    }

    public boolean deleteStateById(String id) {
        return this.deleteEntityById(this.repo, id);
    }

    public StateNode findOriginalState(StateType stateType, ElectionType electionType) {
        return repo.queryFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Set<DistrictNode> fetchStateDistricts(StateNode state) {
        state.getChildren().size();
        return state.getChildren();
    }

//    @Transactional(propagation = Propagation.REQUIRED)
//    public StateNode findOriginalStateWithChildren(StateType stateType, ElectionType electionType) {
//        StateNode state = repo.queryFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
//        for (DistrictNode district : state.getChildren()) {
//            Hibernate.initialize(district.getChildren());
//        }
//        return state;
//    }
//
    public StateNode findOriginalStateEntityGraph(StateType stateType, ElectionType electionType) {
        return repo.getFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType.ORIGINAL, stateType, electionType);
    }

}
