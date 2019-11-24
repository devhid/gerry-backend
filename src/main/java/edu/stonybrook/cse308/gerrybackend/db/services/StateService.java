package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService extends EntityService<StateNode> {

    @Autowired
    private StateRepository repo;

    public List<StateNode> getAllStates(){
        return this.getAllEntities(this.repo);
    }

    public StateNode getStateById(String id){
        return this.getEntityById(this.repo, id);
    }

    public StateNode createState(StateNode state){
        return this.createEntity(this.repo, state);
    }

    public boolean deleteStateById(String id){
        return this.deleteEntityById(this.repo, id);
    }

    public StateNode findOriginalStateByStateType(StateType stateType){
        return repo.findOriginalStateByStateType(stateType);
    }
}
