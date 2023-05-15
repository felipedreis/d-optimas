package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

//TODO: Fix elements name
public class FunctionSolution extends Solution<FunctionSolutionElement, Double, FunctionProblem> {

    private static final Logger logger = LoggerFactory.getLogger(FunctionSolution.class);

    public FunctionSolution(Problem problem) {
        super(problem);
    }

    public FunctionSolution(FunctionSolution s){
        super(s);
    }

    @Override
    public void initialize() {
        functionValue = Double.MAX_VALUE;
    }

    @Override
    public void generateInitialSolution() {

    }

    @Override
    public int compareTo(Solution o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void objectiveFunction() {
        List<Double> vector = solutionsVector.stream()
                .map(FunctionSolutionElement::getValue)
                .collect(Collectors.toList());
        functionValue = getProblem().getFunctionValueOnPoint(vector);
        logger.debug(String.format("Calculated function value of %s for solution %s", functionValue, getId()));
    }

    protected void checkViability() {
        if (solutionsVector == null) {
            viable = false;
            return;
        }

        viable = true;

        for (int i = 0; i < getProblem().getDimension() && viable; ++i) {
            FunctionSolutionElement element = solutionsVector.get(i);
            Double min = getProblem().domain(i, 0);
            Double max = getProblem().domain(i, 1);
            viable = element.getValue() >= min  && element.getValue() <= max;
        }
    }

    @Override
    public Double adaptiveFunctionValue(FunctionSolutionElement element) {
        if (getSolutionsVector().size() == getProblem().getDimension())
            return null;

        Double min = getProblem().domain(getSolutionsVector().size(), 0);
        Double max = getProblem().domain(getSolutionsVector().size(), 1);

        if (element.getValue() < min || element.getValue() > max)
            return null;

        List<Double> elements = new ArrayList<>(getProblem().getInitialPoint());

        for (int i = 0; i < getSolutionsVector().size(); ++i)
            elements.set(i, getSolutionsVector().get(i).getValue());

        elements.set(getSolutionsVector().size(), element.getValue());

        return getProblem().getFunction().getFunctionValue(elements);
    }

    @Override
    public Double getDeviation(Double valor, Double variation, Integer variable) {
        double desvio = variation;
        FunctionProblem problema = getProblem();

        Double limiteInf = problema.domain(variable, 0);
        Double limiteSup = problema.domain(variable, 1);
        Random rand = new Random();
        if ((variation + valor) > limiteSup) {
            desvio = Math.pow(-1, rand.nextInt(2)) * (limiteSup - valor) * rand.nextDouble();
        }
        if ((variation + valor) < limiteInf) {
            desvio = Math.pow(-1, rand.nextInt(2)) * (valor - limiteInf) * rand.nextDouble();
        }
        return desvio;
    }

    @Override
    public Object clone() {
        return new FunctionSolution(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getId());
        b.append('[');
        b.append(Arrays.toString(solutionsVector.toArray()));
        b.append(", ");
        b.append("] = ");
        b.append(functionValue);


        return b.toString();
    }
}
