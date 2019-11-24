package edu.stonybrook.cse308.gerrybackend.db.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class EntityService<T> {

    protected List<T> getAllEntities(JpaRepository<T, String> repo){
        return new ArrayList<>(repo.findAll());
    }

    protected T getEntityById(JpaRepository<T,String> repo, String id){
        Optional<T> entity = repo.findById(id);
        return entity.orElse(null);
    }

    protected T createEntity(JpaRepository<T,String> repo, T entity){
        repo.save(entity);
        return entity;
    }

    protected boolean deleteEntityById(JpaRepository<T,String> repo, String id){
        Optional<T> entity = repo.findById(id);
        if (entity.isPresent()){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

}
