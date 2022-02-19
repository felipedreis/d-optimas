package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class RastriginFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
     
        value = 20 + Math.pow(valX,2) - 10*Math.cos(2*Math.PI*valX);
        value = value + Math.pow(valY,2) - 10*Math.cos(2*Math.PI*valY);
        
        return value;
    }
}
