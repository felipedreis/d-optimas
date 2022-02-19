package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PSOPositionCalcFunctionProblemModifiesSolutionCollections
        extends ModifiesSolutionCollections<FunctionProblem, FunctionSolution> {

    public PSOPositionCalcFunctionProblemModifiesSolutionCollections(Problem problem) {
        super(problem);
    }

    @Override
    public List<FunctionSolution> modify(List<FunctionSolution> solutionsList,
                                         Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {

        List<FunctionSolution> velocities = solutionsList.subList(0, solutionsCount);
        List<FunctionSolution> particles = solutionsList.subList(solutionsCount, 2 * solutionsCount);
        List<FunctionSolution> result = new ArrayList<>();

        for (int i = 0; i < solutionsCount; ++i) {
            Solution solution = Solution.buildSolution(problem);
            Solution particle = particles.get(i);
            Solution velocity = velocities.get(i);
            for (int j = 0; j < problem.getDimension(); ++j) {
                double value = particle.getElement(j).toDoubleValue() + velocity.getElement(j).toDoubleValue();
                solution.addElement(new FunctionSolutionElement(value));
            }

            result.add((FunctionSolution) solution);

        }

        return result;
    }
}
