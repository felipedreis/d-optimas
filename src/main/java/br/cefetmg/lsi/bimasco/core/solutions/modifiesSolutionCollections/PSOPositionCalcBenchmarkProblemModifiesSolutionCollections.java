package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PSOPositionCalcBenchmarkProblemModifiesSolutionCollections
        extends ModifiesSolutionCollections<BenchmarkProblem, BenchmarkSolution> {

    public PSOPositionCalcBenchmarkProblemModifiesSolutionCollections(Problem problem) {
        super(problem);
    }

    @Override
    public List<BenchmarkSolution> modify(List<BenchmarkSolution> solutionsList,
                                         Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {

        List<BenchmarkSolution> velocities = solutionsList.subList(0, solutionsCount);
        List<BenchmarkSolution> particles = solutionsList.subList(solutionsCount, 2 * solutionsCount);
        List<BenchmarkSolution> result = new ArrayList<>();

        for (int i = 0; i < solutionsCount; ++i) {
            Solution solution = Solution.buildSolution(problem);
            Solution particle = particles.get(i);
            Solution velocity = velocities.get(i);
            for (int j = 0; j < problem.getDimension(); ++j) {
                double value = particle.getElement(j).toDoubleValue() + velocity.getElement(j).toDoubleValue();
                solution.addElement(new FunctionSolutionElement(value));
            }

            result.add((BenchmarkSolution) solution);

        }

        return result;
    }
}
