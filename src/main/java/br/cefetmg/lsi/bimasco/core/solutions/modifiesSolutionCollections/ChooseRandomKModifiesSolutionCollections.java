package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChooseRandomKModifiesSolutionCollections extends ModifiesSolutionCollections <Problem,Solution> {
    private RandomDataGenerator generator;

    public ChooseRandomKModifiesSolutionCollections(Problem problem) {
        super(problem);
        this.generator = new RandomDataGenerator();
    }

    @Override
    public List<Solution> modify(List<Solution> solutionsList, Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {
        Object [] sample = generator.nextSample(solutionsList, solutionsCount);
        List<Solution> selectedSolutions = Arrays.stream(sample)
                .map(s -> (Solution) s)
                .collect(Collectors.toList());
        return selectedSolutions;
    }
}
