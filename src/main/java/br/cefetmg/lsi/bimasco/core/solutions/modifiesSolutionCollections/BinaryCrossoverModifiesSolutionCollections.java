package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinaryCrossoverModifiesSolutionCollections extends ModifiesSolutionCollections <Problem, Solution> {

    private UniformIntegerDistribution random;

    public BinaryCrossoverModifiesSolutionCollections(Problem problem) {
        super(problem);
        random = new UniformIntegerDistribution(0, problem.getDimension());
    }

    @Override
    public List<Solution> modify(List<Solution> solutionsList, Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {

        int cut = random.sample();

        List<Object> elem1, elem2;
        List<Object> father1, father2;
        elem1 = new ArrayList<>();
        elem2 = new ArrayList<>();
        father1 = solutionsList.get(0).getSolutionsVector();
        father2 = solutionsList.get(1).getSolutionsVector();

        for (int i = 0; i < problem.getDimension(); ++i) {
            if (i < cut) {
                elem1.add(father1.get(i));
                elem2.add(father2.get(i));
            } else {
                elem1.add(father2.get(i));
                elem2.add(father1.get(i));
            }
        }
        Solution s1 = Solution.buildSolution(problem);
        Solution s2 = Solution.buildSolution(problem);
        s1.setSolutionsVector(elem1);
        s2.setSolutionsVector(elem2);
        return List.of(s1, s2);
    }
}
