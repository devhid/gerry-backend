package edu.stonybrook.cse308.gerrybackend.data.jobs;

import edu.stonybrook.cse308.gerrybackend.enums.types.AlgPhaseType;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
import lombok.Getter;

import javax.persistence.*;

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

    private int iteration;

    public Job() { }

    public Job(AlgPhaseType algPhaseType, StateNode state) {
        this.id = state.getId();
        this.algPhaseType = algPhaseType;
        this.state = state;
        this.iteration = 0;
    }

    public int getNextIteration() {
        return this.iteration++;
    }

}
