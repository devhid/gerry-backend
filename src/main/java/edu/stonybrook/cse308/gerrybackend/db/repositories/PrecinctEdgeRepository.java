package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecinctEdgeRepository extends JpaRepository<PrecinctEdge, String> {
}
