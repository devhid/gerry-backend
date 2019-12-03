package edu.stonybrook.cse308.gerrybackend.data.weights;

import edu.stonybrook.cse308.gerrybackend.enums.measures.MeasureInterface;
import lombok.Getter;

public abstract class MeasureWeight<M extends MeasureInterface> {

    @Getter
    protected M measure;

    @Getter
    protected double weight;

    MeasureWeight(M measure, double weight) {
        this.measure = measure;
        this.weight = weight;
    }

    public boolean isValid() {
        return (this.measure != null);
    }

}
