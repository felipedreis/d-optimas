package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;

public class BenchmarkSolutionAnalyser extends SolutionAnalyser<BenchmarkProblem, BenchmarkSolution> {
    public BenchmarkSolutionAnalyser(BenchmarkProblem problem) {
        super(problem);
    }

    @Override
    public BenchmarkSolution getBestSolution(BenchmarkSolution left, BenchmarkSolution right) {
        return null;
    }

    @Override
    public int compareFunctionValue(Number left, Number right) {
        return 0;
    }
}
