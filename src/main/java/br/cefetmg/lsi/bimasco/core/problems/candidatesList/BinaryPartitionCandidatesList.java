package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.BinaryPartitionNumberProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryPartitionCandidatesList
        extends CandidatesList<BinaryPartitionNumberProblem, BinaryPartitionNumberElementSolution> {

    private RandomDataGenerator rnd;

    public BinaryPartitionCandidatesList(BinaryPartitionNumberProblem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<BinaryPartitionNumberElementSolution> getCandidates() {
        return IntStream.range(0, 2*problem.getWeights().size())
                .boxed()
                .map(i -> i % 2 == 0)
                .map(BinaryPartitionNumberElementSolution::new)
                .collect(Collectors.toList());
    }
}
