package edu.stonybrook.cse308.gerrybackend.db.services;

import edu.stonybrook.cse308.gerrybackend.db.repositories.PrecinctRepository;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrecinctService {

    @Autowired
    private PrecinctRepository precinctRepo;

    @Transactional
    public void add(PrecinctNode precinct){
        precinctRepo.add(precinct);
    }

    @Transactional(readOnly=true)
    public List<PrecinctNode> listPrecincts(){
        return precinctRepo.listPrecincts();
    }

}
