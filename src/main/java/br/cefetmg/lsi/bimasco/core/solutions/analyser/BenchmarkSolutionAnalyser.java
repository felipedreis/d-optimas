package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;

import java.util.Objects;

public class BenchmarkSolutionAnalyser extends SolutionAnalyser<BenchmarkProblem, BenchmarkSolution> {
    public BenchmarkSolutionAnalyser(BenchmarkProblem problem) {
        super(problem);
    }

    @Override
    public BenchmarkSolution getBestSolution(BenchmarkSolution left, BenchmarkSolution right) {
        if (Objects.isNull(left) && Objects.isNull(right))
            throw new IllegalStateException("Both solutions can't be null");

        if (Objects.isNull(left))
            return right;

        if (Objects.isNull(right))
            return left;

        return left.compareTo(right) <= 0 ? left : right;
    }

    @Override
    public int compareFunctionValue(Number left, Number right) {
        if (Objects.isNull(left) && Objects.isNull(right))
            throw new IllegalStateException("Both solutions can't be null");

        if (Objects.isNull(left))
            return 1;

        if (Objects.isNull(right))
            return -1;

        return left.doubleValue() <= right.doubleValue() ? -1 : 1;
    }
}
