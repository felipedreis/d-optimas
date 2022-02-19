package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FunctionSolutionAnalyser extends SolutionAnalyser<FunctionProblem, FunctionSolution> {

    private static final Logger logger = LogManager.getLogger(FunctionSolutionAnalyser.class);

    public FunctionSolutionAnalyser(FunctionProblem problem) {
        super(problem);
    }

    @Override
    public FunctionSolution getBestSolution(FunctionSolution left, FunctionSolution right) {
        if (left == null && right == null)
            throw new IllegalArgumentException();
        else if (left == null)
            return right;
        else if (right == null)
            return left;

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
