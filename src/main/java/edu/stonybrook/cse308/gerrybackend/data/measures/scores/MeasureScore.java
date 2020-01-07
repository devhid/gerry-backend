package edu.stonybrook.cse308.gerrybackend.data.measures.scores;

import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureInterface;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class MeasureScore<M extends MeasureInterface> {

    @Getter
    protected M measure;

    @Getter
    protected double score;

    MeasureScore(M measure, double score) {
        this.measure = measure;
        this.score = score;
    }

}