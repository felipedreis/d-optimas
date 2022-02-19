package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class SumSquaresFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double val1 = points.get(0);
        double val2 = points.get(1);
        double val3 = points.get(2);
        double val4 = points.get(3);
        double val5 = points.get(4);
     
        value = Math.pow(val1,2) +  2*Math.pow(val2,2) +  3*Math.pow(val3,2);
        value = value +  4*Math.pow(val4,2) +  5*Math.pow(val5,2);
        
        return value;
    }
}
