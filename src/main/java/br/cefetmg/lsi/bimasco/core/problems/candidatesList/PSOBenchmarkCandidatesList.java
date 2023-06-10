package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

public class PSOBenchmarkCandidatesList extends CandidatesList<BenchmarkProblem, FunctionSolutionElement> {

    private PSOFunctionCandidatesList psoFunctionCandidateList;

    public PSOBenchmarkCandidatesList(BenchmarkProblem problem) {
        super(problem);
        psoFunctionCandidateList = new PSOFunctionCandidatesList(problem.asFunctionProblem());
    }

    @Override
    public List<FunctionSolutionElement> getCandidates() {
        return psoFunctionCandidateList.getCandidates();
    }
}
