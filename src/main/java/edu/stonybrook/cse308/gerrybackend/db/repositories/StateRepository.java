package edu.stonybrook.cse308.gerrybackend.db.repositories;

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
    @Query("SELECT s FROM StateNode s WHERE s.nodeType = :nodeType and s.stateType = :stateType")
    Collection<StateNode> findStateByNodeTypeAndStateType(@Param("nodeType") NodeType nodeType,
                                                          @Param("stateType") StateType stateType);

    default StateNode findOriginalStateByStateType(StateType stateType) {
        Collection<StateNode> stateNodes = findStateByNodeTypeAndStateType(NodeType.ORIGINAL, stateType);
        return stateNodes.iterator().next();
    }
}
