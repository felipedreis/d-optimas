package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class XinSheYang03Function implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
        
        value = Math.exp(-(Math.pow(valX/15, 6) + Math.pow(valY/15, 6)));
        value = value - 2*Math.exp(-(Math.pow(valX, 2) + Math.pow(valY, 2)))*Math.pow(Math.cos(valX), 2)*Math.pow(Math.cos(valY), 2);
        
        return value;
    }
    
}
