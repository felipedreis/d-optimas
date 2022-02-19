package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class FunctionCandidatesList extends CandidatesList<FunctionProblem, FunctionSolutionElement> {

    private RandomDataGenerator rnd;

    private static final int MAX_VALUES = 10;

    public FunctionCandidatesList(FunctionProblem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<FunctionSolutionElement> getCandidates() {
        List<FunctionSolutionElement> elements = new ArrayList<>();

        for (int i = 0; i < problem.getDimension(); ++i) {
            Double minValue = problem.dominio(i, 0);
            Double maxValue = problem.dominio(i, 1);
            for (int j = 0; j < MAX_VALUES; ++j) {
                elements.add(new FunctionSolutionElement(rnd.nextUniform(minValue, maxValue)));
            }
        }

        return elements;
    }
}
