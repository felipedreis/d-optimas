package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PSOVelocityCalcFunctionProblemModifiesSolutionCollections extends ModifiesSolutionCollections<FunctionProblem, FunctionSolution> {

    private RandomDataGenerator rnd;

    public PSOVelocityCalcFunctionProblemModifiesSolutionCollections(Problem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<FunctionSolution> modify(List<FunctionSolution> solutionsList, Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {


        List<FunctionSolution> newVelocities = new ArrayList<>();

        List<FunctionSolution> velocities = solutionsList.subList(0, solutionsCount);
        List<FunctionSolution> particles = solutionsList.subList(solutionsCount, 2 * solutionsCount);
        List<FunctionSolution> particlesBest = solutionsList.subList(solutionsCount * 2, solutionsCount * 3);
        Solution globalBest = solutionsList.get(solutionsCount * 3);

        double C1 = (double) metaHeuristicParameters.get("C1"), C2 = (double) metaHeuristicParameters.get("C2");

        for (int i = 0; i < solutionsCount; i++) {
            Solution solution = Solution.buildSolution(problem);

            FunctionSolution velocity = velocities.get(i), particle = particles.get(i), particleBest = particlesBest.get(i);

            for (int j = 0; j < problem.getDimension(); ++j) {
                Double element = velocity.getElement(j).toDoubleValue()
                        + C1 * rnd.nextUniform (0, 1) * (particleBest.getElement(j).toDoubleValue() - particle.getElement(j).toDoubleValue())
                        + C2 * rnd.nextUniform(0, 1) * (globalBest.getElement(j).toDoubleValue() - particle.getElement(j).toDoubleValue());

                solution.addElement(new FunctionSolutionElement(element));
            }

            newVelocities.add((FunctionSolution) solution);

        }

        return newVelocities;
    }
}
