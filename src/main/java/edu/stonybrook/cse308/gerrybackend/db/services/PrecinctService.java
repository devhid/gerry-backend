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
    private PrecinctRepository precinctRepo;

    public List<PrecinctNode> getAllPrecincts(){
        List<PrecinctNode> allPrecincts = precinctRepo.findAll();
        if (allPrecincts.size() > 0){
            return allPrecincts;
        }
        else {
            return new ArrayList<>();
        }
    }

    public PrecinctNode getPrecinctById(String id){
        Optional<PrecinctNode> precinct = precinctRepo.findById(id);
        if (precinct.isPresent()){
            return precinct.get();
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public PrecinctNode createPrecinct(PrecinctNode entity){
        precinctRepo.save(entity);
        return entity;
    }

    public void deletePrecinctById(String id){
        Optional<PrecinctNode> precinct = precinctRepo.findById(id);
        if (precinct.isPresent()){
            precinctRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

}
