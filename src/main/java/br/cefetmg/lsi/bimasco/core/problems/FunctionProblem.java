package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.problems.functions.Function;
import br.cefetmg.lsi.bimasco.core.problems.functions.FunctionHelper;

import java.util.ArrayList;
import java.util.List;

//TODO: analyse type of returns
public class FunctionProblem extends Problem {
    private List<Double> initialPoint;
    //TODO: step should be an algorithm parameter
    @Deprecated
    private double step;
    private ArrayList<Object> parameters;
    private double upperBound;
    private Function function;
    private Double[][] domain;

    public static double DEFAULT_UPPER_BOUND = 99999.0;

    public FunctionProblem(){
        super();
    }

    //TODO: Check if this method is really correct because I make some decisions
    //TODO: Maybe we need iterate for all solutions passed on problemData
    @Override
    public void initialize() {
        List<List> problemData =  this.getProblemSettings().getProblemData();

        String functionName = (String) problemData.get(0).get(0);
        dimension = (int) problemData.get(1).get(0);
        step = ((Number) problemData.get(2).get(0)).doubleValue();

        initialPoint = new ArrayList<Double>();
        domain = new Double[dimension][2];

        ArrayList<Double> aux;

        int counter = 3;
        if( problemData.size() >= 3 ){
            for(int i = 0; i< dimension; i++){
                initialPoint.add(((Number) problemData.get(counter).get(i)).doubleValue());
            }

            counter++;

            for(int i = 0; i< dimension; i++){
                domain[i][0] = ((Number)problemData.get(counter).get(0)).doubleValue();
                domain[i][1] = ((Number)problemData.get(counter).get(1)).doubleValue();
            }
        } else{
            initialPoint = null;
            domain = null;
        }

        function = FunctionHelper.buildFunction(functionName);

        if(this.getProblemSettings().getMax()) {
            upperBound = -DEFAULT_UPPER_BOUND;
        } else {
            upperBound = DEFAULT_UPPER_BOUND;
        }

        this.parameters = new ArrayList<Object>();
        parameters.add(step);

        for(int i = 0; i< dimension; i++){
            parameters.add(initialPoint.get(i));
        }

        //Totally crazy this statement
        parameters = null;
    }

    @Override
    public Integer getDimension(){
        return dimension;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return getDimension();
    }

    @Override
    public ArrayList<Object> getParameters(){
        parameters = new ArrayList<Object>();
        parameters.add(step);
        
        for(int i = 0; i< dimension; i++){
            parameters.add(initialPoint.get(i));
        }
        
        return parameters;
    }

    @Override
    public Object getFitnessFunction(List<Object> element){
        return Double.parseDouble(element.get(0).toString());
    }

    @Override
    public Object getLimit(){
        return upperBound;
    }

    public List<Double> getInitialPoint(){ return initialPoint; }

    public Double getFunctionValueOnPoint(List<Double> point){
        return function.getFunctionValue(point);
    }

    @Override
    public double getStep(){
        return step;
    }

    public Double dominio(Integer variavel, Integer limite){
        return domain[variavel][limite];
    }

    public Double[][] getDomain() {
        return domain;
    }

    public void setDomain(Double[][] domain) {
        this.domain = domain;
    }

    public Function getFunction() {
        return function;
    }
}