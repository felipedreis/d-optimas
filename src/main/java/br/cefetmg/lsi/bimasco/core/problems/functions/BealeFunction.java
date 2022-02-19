package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class BealeFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
        
        value = Math.pow(1.5 - valX + valX*valY,2);
        value = value + Math.pow(2.25 - valX + valX*Math.pow(valY,2),2);
        value = value + Math.pow(2.625 - valX + valX*Math.pow(valY,3),2);
        
        return value;
    }
    
}
