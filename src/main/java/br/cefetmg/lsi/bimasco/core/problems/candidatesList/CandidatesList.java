package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Problem;

import java.io.Serializable;
import java.util.List;

public abstract class CandidatesList<P extends Problem, El extends Element> implements Serializable {

    protected P problem;

    public CandidatesList(P problem){
        this.problem = problem;
    }

    public abstract List<El> getCandidates();

    public void setProblem(P problem) {
        this.problem = problem;
    }
}
