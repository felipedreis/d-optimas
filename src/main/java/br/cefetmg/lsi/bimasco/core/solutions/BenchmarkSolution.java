package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.util.EuclideanVector;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BenchmarkSolution extends Solution<FunctionSolutionElement, EuclideanVector<Double>, BenchmarkProblem> {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkSolution.class);

    private static final Comparator<BenchmarkSolution> benchmarkSolutionComparator =
            Comparator.comparingDouble(s -> s.getFunctionValue().doubleValue());

    public BenchmarkSolution(Problem problem) {
        super(problem);
    }

    public BenchmarkSolution(Solution<FunctionSolutionElement, EuclideanVector<Double>, BenchmarkProblem> another) {
        super(another);
    }

    @Override
    public void initialize() {
        functionValue = new EuclideanVector<>(Double.MAX_VALUE);
    }

    @Override
    public void generateInitialSolution() {

    }

    @Override
    protected void objectiveFunction() {
        logger.debug("Evaluating the objective function for {}", this);
        double [] y = getProblem().evaluateFunction(toDoubleArray());

        functionValue = new EuclideanVector<>(ArrayUtils.toObject(y));
    }

    @Override
    protected void checkViability() {
        if (solutionsVector == null || solutionsVector.size() < getProblem().getDimension()) {
            viable = false;
            return;
        }

        viable = true;

        for (int i = 0; i < solutionsVector.size(); ++i) {
            double v = solutionsVector.get(i).toDoubleValue();

            viable = viable &&
                    v >= getProblem().getSmallestValueOfInterest(i) &&
                    v <= getProblem().getLargestValueOfInterest(i);
        }

        if (getProblem().getNumberOfConstraints() == 0)
            return;

        double [] constraints = getProblem().evaluateConstraint(toDoubleArray());

        for (int i = 0; i < constraints.length; ++i) {
            // TODO check constraints for constrained optimizaition problems
        }
    }

    @Override
    public EuclideanVector<Double> adaptiveFunctionValue(FunctionSolutionElement value) {

        if (getSolutionsVector().size() == getProblem().getDimension())
            return null;

        ArrayList<Double> elements = IntStream.range(0, getProblem().getDimension())
                .mapToObj(x -> 0.0)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < getSolutionsVector().size(); ++i)
            elements.set(i, getSolutionsVector().get(i).getValue());

        elements.set(getSolutionsVector().size(), value.getValue());
        double [] doubleElements = elements.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        Double [] evaluationResult = ArrayUtils.toObject(getProblem().evaluateFunction(doubleElements));

        return new EuclideanVector<>(evaluationResult);
    }

    @Override
    public Double getDeviation(Double valor, Double variation, Integer variable) {
        double desvio = variation;
        BenchmarkProblem problema = getProblem();

        Double limiteInf = problema.getSmallestValueOfInterest(variable);
        Double limiteSup = problema.getLargestValueOfInterest(variable);
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
        return new BenchmarkSolution(this);
    }

    @Override
    public int compareTo(Solution o) {
        return benchmarkSolutionComparator.compare(this, (BenchmarkSolution) o);
    }
}
