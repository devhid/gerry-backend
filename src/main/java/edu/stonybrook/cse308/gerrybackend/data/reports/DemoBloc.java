package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import lombok.Getter;

public class DemoBloc {

    @Getter
    private DemographicType demographicType;

    @Getter
    private int demographicPopulation;

    @Getter
    private int totalPop;

    public DemoBloc(DemographicType demographicType, int demographicPopulation, int totalPop) {
        this.demographicType = demographicType;
        this.demographicPopulation = demographicPopulation;
        this.totalPop = totalPop;
    }
}
