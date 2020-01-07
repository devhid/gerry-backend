package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.graph.edges.DistrictEdge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictEdgeRepository extends JpaRepository<DistrictEdge, String> {
}
