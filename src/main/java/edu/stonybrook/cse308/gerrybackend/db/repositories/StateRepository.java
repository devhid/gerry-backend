package edu.stonybrook.cse308.gerrybackend.db.repositories;

import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.StateType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StateRepository extends JpaRepository<StateNode, String> {
    @Query("SELECT s FROM StateNode s WHERE s.nodeType = :nodeType and s.stateType = :stateType and s.electionData.electionType = :electionType")
    Collection<StateNode> findOriginalStateHelper(@Param("nodeType") NodeType nodeType,
                                                  @Param("stateType") StateType stateType,
                                                  @Param("electionType") ElectionType electionType);

    default StateNode findOriginalState(StateType stateType, ElectionType electionType) {
        Collection<StateNode> stateNodes = findOriginalStateHelper(NodeType.ORIGINAL, stateType, electionType);
        if (stateNodes.size() == 0) {
            return null;
        }
        return stateNodes.iterator().next();
    }
}
