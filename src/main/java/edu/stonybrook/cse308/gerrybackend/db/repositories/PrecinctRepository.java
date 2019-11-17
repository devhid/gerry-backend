package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrecinctRepository extends JpaRepository<PrecinctNode, String> {

}
