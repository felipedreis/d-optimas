package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.Knapsack01Problem;
import br.cefetmg.lsi.bimasco.core.solutions.Knapsack01Solution;

public class Knapsack01SolutionAnalyser extends SolutionAnalyser<Knapsack01Problem, Knapsack01Solution> {

    public Knapsack01SolutionAnalyser(Knapsack01Problem problem) {
        super(problem);
    }

    @Override
    public Knapsack01Solution getBestSolution(Knapsack01Solution left, Knapsack01Solution right) {
        return compareFunctionValue(left.getFunctionValue(), right.getFunctionValue()) < 0 ? left : right;
    }

    @Override
    public int compareFunctionValue(Number left, Number right) {
        double leftValue = left.doubleValue();
        double rightValue = right.doubleValue();
        double tolerance = 1e-5;

        if (Math.abs(leftValue - rightValue) < tolerance)
            return 0;

        if (leftValue < rightValue)
            return -1;
        else
            return 1;
    }

}
