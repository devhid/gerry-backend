package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.db.repositories.StateRepository;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    @Autowired
    private StateRepository repo;

    public List<StateNode> getAllStates(){
        List<StateNode> allEntities = repo.findAll();
        if (allEntities.size() > 0){
            return allEntities;
        }
        else {
            return new ArrayList<>();
        }
    }

    public StateNode getStateById(String id){
        Optional<StateNode> entity = repo.findById(id);
        if (entity.isPresent()){
            return entity.get();
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public StateNode createState(StateNode entity){
        repo.save(entity);
        return entity;
    }

    public void deleteStateById(String id){
        Optional<StateNode> entity = repo.findById(id);
        if (entity.isPresent()){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public StateNode findOriginalStateByStateType(StateType stateType){
        return repo.findOriginalStateByStateType(stateType);
    }
}
