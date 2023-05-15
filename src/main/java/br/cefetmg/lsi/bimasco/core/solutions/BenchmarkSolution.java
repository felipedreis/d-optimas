package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.util.EuclideanVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkSolution extends Solution<FunctionSolutionElement, EuclideanVector<Double>, BenchmarkProblem> {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkSolution.class);

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
        // TODO implement the evaluation of objective function properly
        logger.debug("Evaluating the objective function for {}", this);
        getProblem().evaluateFunction(toDoubleArray());
    }

    @Override
    protected void checkViability() {
        // TODO implement this properly
        double[] solutionValues = toDoubleArray();
        viable = false;

        if (solutionValues.length == getProblem().getDimension()) {
            double [] constraints = getProblem().evaluateConstraint(toDoubleArray());
            viable = true;
        }
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
    }
}
