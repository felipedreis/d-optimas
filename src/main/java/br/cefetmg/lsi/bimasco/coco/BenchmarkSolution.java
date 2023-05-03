package br.cefetmg.lsi.bimasco.coco;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.util.EuclideanVector;

public class BenchmarkSolution extends Solution<FunctionSolutionElement, EuclideanVector<Double>, BenchmarkProblem> {

    public BenchmarkSolution(Problem problem) {
        super(problem);
    }

    public BenchmarkSolution(Solution<FunctionSolutionElement, EuclideanVector<Double>, BenchmarkProblem> another) {
        super(another);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void generateInitialSolution() {

    }

    @Override
    protected void objectiveFunction() {
        getProblem().evaluateFunction(toDoubleArray());
    }

    @Override
    protected void checkViability() {
        getProblem().evaluateConstraint(toDoubleArray());
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
    }
}
