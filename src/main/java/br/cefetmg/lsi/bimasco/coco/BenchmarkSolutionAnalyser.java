package br.cefetmg.lsi.bimasco.coco;

import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;

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
