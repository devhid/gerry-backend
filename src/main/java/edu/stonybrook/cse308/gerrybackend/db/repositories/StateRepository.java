package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateNode, String> {
    StateNode queryFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType nodeType, StateType stateType, ElectionType electionType);

    @EntityGraph(value = "state-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    StateNode getFirstByNodeTypeAndStateTypeAndElectionData_ElectionType(NodeType nodeType, StateType stateType, ElectionType electionType);
}
