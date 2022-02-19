package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.problems.functions.Function;
import br.cefetmg.lsi.bimasco.core.problems.functions.FunctionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MOProblem extends Problem {

    private List<Function> functions;
    private List<Double> variablesUpperLimits;
    private List<Double> variablesLowerLimits;
    private List<Double> objectiveUpperLimits;
    private List<Double> objectiveLowerLimits;

    /*
    [[2],                               <- dimension
    ["EggHolder", "XingXenYang"],       <- objectives
    [-10, -10],                         <- domain lower limits
    [10, 10],                           <- domain upper limits
    [-100, -100],                       <- image lower limits
    [100, 100]]                         <- image upper limits
     */
    @Override
    public void initialize() {

        List<List> problemData = getProblemSettings().getProblemData();
        dimension = (Integer) problemData.get(1).get(0);

        functions = new ArrayList<>();
        for (Object functionName : problemData.get(0)) {
            Function function = FunctionHelper.buildFunction(functionName.toString());
            functions.add(function);
        }

        variablesLowerLimits = new ArrayList<>();
        variablesUpperLimits = new ArrayList<>();
        objectiveLowerLimits = new ArrayList<>();
        objectiveUpperLimits = new ArrayList<>();

        for (Object variableLowerLimit : problemData.get(2)) {
            variablesLowerLimits.add((Double)variableLowerLimit);
        }

        for (Object variableUpperLimit : problemData.get(3)) {
            variablesUpperLimits.add((Double)variableUpperLimit);
        }

        for (Object objectiveLowerLimit : problemData.get(4)) {
            objectiveLowerLimits.add((Double) objectiveLowerLimit);
        }

        for (Object objectiveUpperLimit : problemData.get(5)) {
            objectiveUpperLimits.add((Double) objectiveUpperLimit);
        }

        super.initialize();

    }

    @Override
    public Integer getSolutionElementsCount() {
        return getDimension();
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        return null;
    }

    @Override
    public Object getLimit() {
        return null;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return null;
    }

    @Override
    public Integer getObjectives() {
        return functions.size();
    }

    public List<Double> evaluate(List<Double> x) {
        return functions.stream().map(function -> function.getFunctionValue(x))
                .collect(Collectors.toList());
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public List<Double> getVariablesUpperLimits() {
        return variablesUpperLimits;
    }

    public void setVariablesUpperLimits(List<Double> variablesUpperLimits) {
        this.variablesUpperLimits = variablesUpperLimits;
    }

    public List<Double> getVariablesLowerLimits() {
        return variablesLowerLimits;
    }

    public void setVariablesLowerLimits(List<Double> variablesLowerLimits) {
        this.variablesLowerLimits = variablesLowerLimits;
    }

    public List<Double> getObjectiveUpperLimits() {
        return objectiveUpperLimits;
    }

    public void setObjectiveUpperLimits(List<Double> objectiveUpperLimits) {
        this.objectiveUpperLimits = objectiveUpperLimits;
    }

    public List<Double> getObjectiveLowerLimits() {
        return objectiveLowerLimits;
    }

    public void setObjectiveLowerLimits(List<Double> objectiveLowerLimits) {
        this.objectiveLowerLimits = objectiveLowerLimits;
    }
}
