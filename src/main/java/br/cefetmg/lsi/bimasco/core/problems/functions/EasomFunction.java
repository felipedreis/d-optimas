package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class EasomFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
        
        value = - Math.cos(valX)*Math.cos(valY);
        value = value * Math.exp(- Math.pow(valX-Math.PI,2) - Math.pow(valY-Math.PI,2));
        
        return value;
    }
    
}
