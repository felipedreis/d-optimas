package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//TODO: review all throws
public abstract class ModifiesSolutionCollections <P extends Problem, S extends Solution> implements Serializable {

    protected P problem;

    public ModifiesSolutionCollections(Problem problem) {
        this.problem = (P) problem;
    }

    public abstract List<S> modify(List<S> solutionsList, Map<String, Object> metaHeuristicParameters, Integer solutionsCount);

}
