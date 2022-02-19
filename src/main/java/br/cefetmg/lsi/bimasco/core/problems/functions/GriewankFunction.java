package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class GriewankFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
     
        value = (1/4000.0)*(Math.pow(valX,2) + Math.pow(valY,2));
        value = value - Math.cos(valX)*Math.cos(valY/Math.sqrt(2)) + 1;

        return value;
    }
}
