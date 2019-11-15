package edu.stonybrook.cse308.gerrybackend.data.reports;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;

import java.util.EnumMap;
import java.util.Map;

public class DemoBloc {

    private Map<DemographicType,Integer> demoBlocPopulation;
    private int totalPop;

    public DemoBloc(int totalPop){
        this.demoBlocPopulation = new EnumMap<>(DemographicType.class);
        this.totalPop = totalPop;
    }

    public int size(){
        return this.demoBlocPopulation.size();
    }

    public void setDemoBlocPopulation(DemographicType demoType, int demoPop){
        this.demoBlocPopulation.put(demoType, demoPop);
    }

}
