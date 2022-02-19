package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class MichalewicsFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
     
        value = -1*Math.sin(valX)*Math.pow(Math.sin(1*Math.pow(valX,2)/Math.PI),20);
        value = value - Math.sin(valY)*Math.pow(Math.sin(2*Math.pow(valY,2)/Math.PI),20);
        
        return value;
    }
}
