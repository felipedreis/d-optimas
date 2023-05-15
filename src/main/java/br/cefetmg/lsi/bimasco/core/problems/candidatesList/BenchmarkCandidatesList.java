package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkCandidatesList extends CandidatesList <BenchmarkProblem, FunctionSolutionElement> {

    private static final int MAX_VALUES = 10;

    private RandomDataGenerator rnd;

    public BenchmarkCandidatesList(BenchmarkProblem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<FunctionSolutionElement> getCandidates() {

        List<FunctionSolutionElement> elements = new ArrayList<>();

        for (int i = 0; i < problem.getDimension(); ++i) {
            Double minValue = problem.getSmallestValueOfInterest(i);
            Double maxValue = problem.getLargestValueOfInterest(i);
            for (int j = 0; j < MAX_VALUES; ++j) {
                elements.add(new FunctionSolutionElement(rnd.nextUniform(minValue, maxValue)));
            }
        }

        return elements;
    }
}
