package edu.stonybrook.cse308.gerrybackend.data.jobs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.stonybrook.cse308.gerrybackend.communication.dto.statistics.StateNodeStatistics;
import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

import javax.persistence.*;

@JsonIgnoreProperties({"state"})
@Entity
public class Job {

    @Getter
    @Id
    @Column(name = "id")
    private String id;  // same as state id b/c of @MapsId

    @Getter
    private AlgPhaseType algPhaseType;

    @Getter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    private StateNode state;

    private int phaseOneIteration;

    private int phaseTwoIteration;

    public Job() { }

    public Job(AlgPhaseType algPhaseType, StateNode state) {
        this.id = state.getId();
        this.algPhaseType = algPhaseType;
        this.state = state;
        this.phaseOneIteration = 1;
        this.phaseTwoIteration = 1;
    }

    public int getNextPhaseOneIteration() {
        return this.phaseOneIteration++;
    }

    public int getNextPhaseTwoIteration() {
        return this.phaseTwoIteration++;
    }

    public StateNodeStatistics getStateNodeStatistics() {
        return StateNodeStatistics.fromStateNode(this.state);
    }

}
