package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrecinctService {

    @Autowired
    private PrecinctRepository repo;

    public List<PrecinctNode> getAllPrecincts(){
        List<PrecinctNode> allEntities = repo.findAll();
        if (allEntities.size() > 0){
            return allEntities;
        }
        else {
            return new ArrayList<>();
        }
    }

    public PrecinctNode getPrecinctById(String id){
        Optional<PrecinctNode> entity = repo.findById(id);
        if (entity.isPresent()){
            return entity.get();
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public PrecinctNode createPrecinct(PrecinctNode entity){
        repo.save(entity);
        return entity;
    }

    public void deletePrecinctById(String id){
        Optional<PrecinctNode> entity = repo.findById(id);
        if (entity.isPresent()){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

}
