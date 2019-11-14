package edu.stonybrook.cse308.gerrybackend.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class EntityService<T> {

    @Autowired
    private JpaRepository<T,String> repo;

    public List<T> getAllEntities(){
        List<T> allEntities = repo.findAll();
        if (allEntities.size() > 0){
            return allEntities;
        }
        else {
            return new ArrayList<>();
        }
    }

    public T getEntityById(String id){
        Optional<T> entity = repo.findById(id);
        if (entity.isPresent()){
            return entity.get();
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }

    public T createEntity(T entity){
        repo.save(entity);
        return entity;
    }

    public void deleteEntityById(String id){
        Optional<T> entity = repo.findById(id);
        if (entity.isPresent()){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Replace this string later!");
        }
    }
}
