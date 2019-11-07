package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class PrecinctRepository {

    @PersistenceContext
    private EntityManager em;

    public void add(PrecinctNode precinct){
        em.persist(precinct);
    }

    public List<PrecinctNode> listPrecincts(){
        CriteriaQuery<PrecinctNode> criteriaQuery = em.getCriteriaBuilder().createQuery(PrecinctNode.class);
        return em.createQuery(criteriaQuery).getResultList();
    }

}
