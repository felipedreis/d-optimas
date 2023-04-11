package br.cefetmg.lsi.bimasco.coco;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

public class BenchmarkSolution extends Solution<FunctionSolutionElement, Double, BenchmarkProblem> {

    public BenchmarkSolution(Problem problem) {
        super(problem);
    }

    public BenchmarkSolution(Solution<FunctionSolutionElement, Double, BenchmarkProblem> another) {
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
