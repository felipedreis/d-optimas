package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class PSOFunctionCandidatesList extends CandidatesList<FunctionProblem, FunctionSolutionElement> {

    private RandomDataGenerator rnd;

    public PSOFunctionCandidatesList(FunctionProblem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<FunctionSolutionElement> getCandidates() {
        List<FunctionSolutionElement> elements = new ArrayList<>();
        for (int i = 0; i < problem.getDimension(); ++i) {
            elements.add(new FunctionSolutionElement(rnd.nextUniform(-1, 1)));
        }

        return elements;
    }
}
