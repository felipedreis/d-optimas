package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA;

import java.util.ArrayList;

public class SATemp {

    private Temperature temperature;
    
    private SATemp(Temperature temperature){
        this.temperature = temperature;
    }
    
    private Object initialTemperature(ArrayList<Object> parameters){
        return temperature.initialTemperature(parameters);
    }
    
    private Object update(Object currentTemperature, Integer iteration){
        return temperature.update(currentTemperature, iteration);
    }
}