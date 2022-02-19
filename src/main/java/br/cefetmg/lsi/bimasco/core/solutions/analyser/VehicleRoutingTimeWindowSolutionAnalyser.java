package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.VehicleRoutingTimeWindowProblem;
import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;

public class VehicleRoutingTimeWindowSolutionAnalyser
        extends SolutionAnalyser<VehicleRoutingTimeWindowProblem, VehicleRoutingTimeWindowSolution> {

    public VehicleRoutingTimeWindowSolutionAnalyser(VehicleRoutingTimeWindowProblem problem) {
        super(problem);
    }

    @Override
    public VehicleRoutingTimeWindowSolution getBestSolution(VehicleRoutingTimeWindowSolution left, VehicleRoutingTimeWindowSolution right) {
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
