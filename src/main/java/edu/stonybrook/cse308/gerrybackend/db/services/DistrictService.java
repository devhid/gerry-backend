package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.DistrictRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DistrictService {
    
    @Autowired
    private DistrictRepository repo;

    public List<DistrictNode> getAllDistricts(){
        List<DistrictNode> allEntities = repo.findAll();
        if (allEntities.size() > 0){
            return allEntities;
        }
        else {
            return new ArrayList<>();
        }
    }

    public DistrictNode getDistrictById(String id){
        Optional<DistrictNode> entity = repo.findById(id);
        if (entity.isPresent()){
            return entity.get();
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public DistrictNode createDistrict(DistrictNode entity){
        repo.save(entity);
        return entity;
    }

    public void deleteDistrictById(String id){
        Optional<DistrictNode> entity = repo.findById(id);
        if (entity.isPresent()){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }
    
}
