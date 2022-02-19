package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.util.List;

public class EggHolderFunction implements Function {
    private double value = 0.0;
    
    @Override
    public Double getFunctionValue(List<Double> points){
        double valX = points.get(0);
        double valY = points.get(1);
        
        value = -(valY + 47)*Math.sin(Math.sqrt(Math.abs(valY + (valX/2.0) + 47)))
                - valX*Math.sin(Math.sqrt(Math.abs(valX - (valY + 47))));
        
        return value;
    }
    
}
